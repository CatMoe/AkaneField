package catmoe.fallencrystal.akanefield.checks;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import catmoe.fallencrystal.akanefield.AkaneFieldProxy;
import catmoe.fallencrystal.akanefield.common.AuthCheckType;
import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.objects.FancyInteger;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListReason;
import catmoe.fallencrystal.akanefield.common.service.VPNService;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.utils.ComponentBuilder;

public class AuthCheckReloaded {
    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antibotManager;
    private final Map<String, AuthCheckType> checking;
    private final Map<String, AuthCheckType> completedCheck;
    private final Map<String, FancyInteger> pingMap;
    private final Map<String, Integer> pingData;
    private final Map<String, FancyInteger> failure;
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledTask> runningTasks;
    private final Map<String, String> checkInitiator;
    private final VPNService VPNService;

    public AuthCheckReloaded(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.antibotManager = plugin.getAntiBotManager();
        this.checking = new HashMap<>();
        this.completedCheck = new HashMap<>();
        this.pingMap = new HashMap<>();
        this.pingData = new HashMap<>();
        this.failure = new HashMap<>();
        this.taskScheduler = ProxyServer.getInstance().getScheduler();
        this.runningTasks = new HashMap<>();
        this.checkInitiator = new HashMap<>();
        this.VPNService = plugin.getVPNService();
        plugin.getLogHelper().debug("Loaded " + this.getClass().getSimpleName() + "!");
    }

    public void onPing(ProxyPingEvent e, String ip) {
        // Se sta eseguendo il ping check allora registra il ping
        if (isCompletingPingCheck(ip)) {
            registerPing(ip);
            // Durante L'antibotMode:
            if (antibotManager.isAntiBotModeEnabled()) {
                int currentIPPings = pingMap.get(ip).get();
                int pingRequired = pingData.get(ip);
                ServerPing ping = e.getResponse();
                ping.getVersion().setProtocol(0);
                // Impostazion e Dell'interfaccia per il conteggio dei ping
                if (currentIPPings == pingRequired) {
                    ping.getVersion().setName(ServerUtil.colorize(MessageManager.verifiedPingInterface));
                } else {
                    ping.getVersion().setName(ServerUtil.colorize(MessageManager.normalPingInterface
                            .replace("[$Current]", String.valueOf(currentIPPings))
                            .replace("[$Required]", String.valueOf(pingRequired))));
                }
                // Imposto la risposta al ping
                e.setResponse(ping);
            }
        }
        // se ha superato il numero massimo di ping allora lo aggiunge nei fails
        if (hasExceededPingLimit(ip)) {
            increaseFails(ip, e.getConnection().getName());
            resetData(ip);
        }
    }

    public void onJoin(PreLoginEvent e, String ip) {
        if (isCompletingPingCheck(ip)) {
            int currentIPPings = pingMap.get(ip).get();
            int pingRequired = pingData.getOrDefault(ip, 0);
            if (pingRequired != 0 && currentIPPings == pingRequired) {
                // 0#133
                String initiator = checkInitiator.getOrDefault(ip, null);
                if (initiator.equals(e.getConnection().getName())) {
                    // checking connection
                    if (ConfigManager.getProxyCheckConfig().isCheckFastJoin() && !hasFailedThisCheck(ip, 2)) {
                        VPNService.submitIP(ip, e.getConnection().getName());
                    }
                    addToPingCheckCompleted(ip);
                    checking.remove(ip);
                } else {
                    resetData(ip);
                    increaseFails(ip, e.getConnection().getName());
                }
            }

            // se i ping sono inferiori quando entra ha fallito
            if (pingRequired != 0 && currentIPPings < pingRequired) {
                increaseFails(ip, e.getConnection().getName());
            }
        }
        if (isWaitingResponse(ip)) {
            resetTotal(ip);
            return;
        }

        int checkTimer = ThreadLocalRandom.current().nextInt(ConfigManager.authMinMaxTimer[0],
                ConfigManager.authMinMaxTimer[1]);
        if (hasCompletedPingCheck(ip)) {
            submitTimerTask(ip, checkTimer);
            e.setCancelReason(
                    ComponentBuilder.buildColorized(MessageManager.getTimerMessage(String.valueOf(checkTimer + 1))));
            e.setCancelled(true);
            return;
        }
        int pingTimer = ThreadLocalRandom.current().nextInt(ConfigManager.authMinMaxPing[0],
                ConfigManager.authMinMaxPing[1]);
        addToCompletingPingCheck(ip, pingTimer);
        checkInitiator.put(ip, e.getConnection().getName());
        increaseFails(ip, e.getConnection().getName());
        e.setCancelReason(ComponentBuilder.buildColorized(
                MessageManager.getPingMessage(String.valueOf(pingTimer))));
        e.setCancelled(true);
    }

    /**
     * Aggiunge una task allo scheduler con le informazioni del
     * player e del tempo che gli server aspettare per completarla
     *
     * @param ip    IP da mettere in coda
     * @param timer tempo in coda
     */
    private void submitTimerTask(String ip, int timer) {
        if (runningTasks.containsKey(ip)) {
            runningTasks.get(ip).cancel();
        }
        ScheduledTask task = taskScheduler.schedule(
                AkaneFieldProxy.getInstance(),
                () -> {
                    addToWaiting(ip);
                    runningTasks.remove(ip);
                },
                1000L * timer,
                TimeUnit.MILLISECONDS);
        runningTasks.put(ip, task);
    }

    private boolean hasExceededPingLimit(String ip) {
        if (pingData.get(ip) == null || pingMap.get(ip) == null) {
            return true;
        }
        return pingMap.get(ip).get() > pingData.get(ip);
    }

    private void resetData(String ip) {
        pingMap.remove(ip);
        checking.remove(ip);
        completedCheck.remove(ip);
        if (runningTasks.containsKey(ip)) {
            runningTasks.get(ip).cancel();
        }
        runningTasks.remove(ip);
        pingData.remove(ip);
        // failure.remove(ip);
    }

    /**
     *
     * @param ip  The ip to check
     * @param min The min amount of times required to fail this check
     * @return if the ip has failed at least x min times this check
     */
    private boolean hasFailedThisCheck(String ip, int min) {
        return failure.getOrDefault(ip, new FancyInteger(0)).get() >= min;
    }

    /**
     * @param ip IP da resettare completamente
     */
    private void resetTotal(String ip) {
        pingMap.remove(ip);
        checking.remove(ip);
        completedCheck.remove(ip);
        runningTasks.remove(ip);
        pingData.remove(ip);
        failure.remove(ip);
        checkInitiator.remove(ip);
    }

    /**
     * @param ip                  IP da aggiungere
     * @param generatedPingAmount Numero di volte che deve pingare il server per
     *                            eseguire un controllo corretto
     */
    private void addToCompletingPingCheck(String ip, int generatedPingAmount) {
        pingMap.put(ip, new FancyInteger(0));
        pingData.put(ip, generatedPingAmount);
        checking.put(ip, AuthCheckType.Motd);
    }

    /**
     * @param ip IP da controllare
     * @return Ritorna se ha completato il check del ping e può procedere con il
     *         resto dei controlli
     */
    private boolean hasCompletedPingCheck(String ip) {
        return completedCheck.get(ip) != null && completedCheck.get(ip).equals(AuthCheckType.Motd);
    }

    /**
     *
     * @param ip IP da controllare
     * @return Ritorna se il player sta eseguendo il ping check
     */
    private boolean isCompletingPingCheck(String ip) {
        return checking.get(ip) != null && checking.get(ip).equals(AuthCheckType.Motd);
    }

    /**
     * @param ip IP da controllare
     * @return Ritorna se il player è stato confermato e può entrare
     */
    private boolean isWaitingResponse(String ip) {
        return completedCheck.get(ip) != null && completedCheck.get(ip).equals(AuthCheckType.WAITING);
    }

    /**
     * Aggiunge l'ip a coloro che quando si connetteranno
     * inizierà il countdown per il join/blacklist
     * 
     * @param ip IP da aggiungere
     */
    private void addToPingCheckCompleted(String ip) {
        completedCheck.put(ip, AuthCheckType.Motd);
    }

    /**
     * Aggiunge l'ip a coloro che quando si connettono
     * entreranno automaticamente nel server
     * 
     * @param ip IP da aggiungere
     */
    private void addToWaiting(String ip) {
        completedCheck.put(ip, AuthCheckType.WAITING);
        plugin.scheduleDelayedTask(() -> {
            completedCheck.remove(ip);
            resetData(ip);
        }, false, ConfigManager.authBetween);
    }

    /**
     * Registra il ping al refresh
     * Aumentando i valori nella pingmap
     * 
     * @param ip IP da controllare
     */
    private void registerPing(String ip) {
        if (pingMap.containsKey(ip)) {
            pingMap.get(ip).increase();
        } else {
            pingMap.put(ip, new FancyInteger(0));
        }
    }

    /**
     * Aumenta il numero di volte che un ip ha fallito
     * il check
     * 
     * @param ip IP a cui si deve aumentare i fails
     */
    public void increaseFails(String ip, String name) {
        FancyInteger current = failure.getOrDefault(ip, new FancyInteger(0));
        current.increase();
        failure.put(ip, current);

        if (current.get() >= ConfigManager.authMaxFails) {
            antibotManager.getBlackListService().blacklist(ip, BlackListReason.CHECK_FAILS, name);
            resetTotal(ip);
        }
    }

}
