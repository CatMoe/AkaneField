package catmoe.fallencrystal.akanefield;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import catmoe.fallencrystal.akanefield.commands.CommandManager;
import catmoe.fallencrystal.akanefield.commands.subcommands.AddRemoveBlacklistCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.AddRemoveWhitelistCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.AttackLogCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.CheckIDCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.ClearCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.DumpCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.FirewallCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.HelpCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.StatsCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.TestMenuCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.TestMessageCommand;
import catmoe.fallencrystal.akanefield.commands.subcommands.ToggleNotificationCommand;
import catmoe.fallencrystal.akanefield.common.FieldRunnable;
import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.IConfiguration;
import catmoe.fallencrystal.akanefield.common.INotificator;
import catmoe.fallencrystal.akanefield.common.IServerPlatform;
import catmoe.fallencrystal.akanefield.common.core.AkaneFieldCore;
import catmoe.fallencrystal.akanefield.common.helper.LogHelper;
import catmoe.fallencrystal.akanefield.common.helper.PerformanceHelper;
import catmoe.fallencrystal.akanefield.common.helper.ServerType;
import catmoe.fallencrystal.akanefield.common.objects.filter.ProxyAttackFilter;
import catmoe.fallencrystal.akanefield.common.service.AttackTrackerService;
import catmoe.fallencrystal.akanefield.common.service.FirewallService;
import catmoe.fallencrystal.akanefield.common.service.UserDataService;
import catmoe.fallencrystal.akanefield.common.service.VPNService;
import catmoe.fallencrystal.akanefield.common.thread.LatencyThread;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.RuntimeUtil;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.common.utils.Version;
import catmoe.fallencrystal.akanefield.listener.CustomEventListener;
import catmoe.fallencrystal.akanefield.listener.HandShakeListener;
import catmoe.fallencrystal.akanefield.listener.MainEventListener;
import catmoe.fallencrystal.akanefield.listener.PingListener;
import catmoe.fallencrystal.akanefield.objects.Config;
import catmoe.fallencrystal.akanefield.utils.Metrics;
import catmoe.fallencrystal.akanefield.utils.Utils;

public final class AkaneFieldProxy extends Plugin implements IAntiBotPlugin, IServerPlatform {
    private static AkaneFieldProxy instance;

    private TaskScheduler scheduler;
    private IConfiguration config;
    private IConfiguration messages;
    private IConfiguration whitelist;
    private IConfiguration blacklist;
    private IAntiBotManager antiBotManager;
    private LatencyThread latencyThread;
    private LogHelper logHelper;
    private FirewallService firewallService;
    private UserDataService userDataService;
    private VPNService VPNService;
    private Notificator notificator;
    private AkaneFieldCore core;
    private AttackTrackerService attackTrackerService;
    private boolean isRunning;

    public void onEnable() {
        instance = this;
        this.isRunning = true;
        PerformanceHelper.init(ServerType.BUNGEECORD);
        RuntimeUtil.setup(this);
        ServerUtil.setPlatform(this);
        long a = System.currentTimeMillis();
        this.scheduler = ProxyServer.getInstance().getScheduler();
        this.config = new Config(this, "%datafolder%/config.yml");
        this.messages = new Config(this, "%datafolder%/messages.yml");
        this.whitelist = new Config(this, "%datafolder%/whitelist.yml");
        this.blacklist = new Config(this, "%datafolder%/blacklist.yml");
        this.logHelper = new LogHelper(this);
        try {
            ConfigManager.init(this.config);
            MessageManager.init(this.messages);
        } catch (Exception e) {
            this.logHelper.error("&c加载配置文件时发生了错误..");
            return;
        }
        Version.init(this);
        new Metrics(this, 11712);
        this.firewallService = new FirewallService(this);
        this.VPNService = new VPNService(this);
        this.VPNService.load();
        this.antiBotManager = new AntiBotManager(this);
        this.antiBotManager.getQueueService().load();
        this.antiBotManager.getWhitelistService().load();
        this.antiBotManager.getBlackListService().load();
        this.attackTrackerService = new AttackTrackerService(this);
        attackTrackerService.load();
        this.firewallService.enable();
        this.latencyThread = new LatencyThread(this);
        this.core = new AkaneFieldCore(this);
        this.core.load();
        this.userDataService = new UserDataService(this);
        this.userDataService.load();
        ProxyServer.getInstance().getLogger().setFilter(new ProxyAttackFilter(this));

        this.notificator = new Notificator();
        this.notificator.init(this);
        this.logHelper.sendLogo();
        this.logHelper.info("&b调度器线程数: &f$1"
                .replace("$1", String.valueOf(Version.getCores())));
        CommandManager commandManager = new CommandManager(this, "akanefield", "", "af", "akanefield");
        commandManager.register(new AddRemoveBlacklistCommand(this));
        commandManager.register(new AddRemoveWhitelistCommand(this));
        commandManager.register(new ClearCommand(this));
        commandManager.register(new DumpCommand(this));
        commandManager.register(new HelpCommand(this));
        commandManager.register(new StatsCommand(this));
        commandManager.register(new ToggleNotificationCommand());
        commandManager.register(new CheckIDCommand(this));
        commandManager.register(new FirewallCommand(this));
        commandManager.register(new AttackLogCommand(this));
        commandManager.register(new TestMessageCommand(this));
        commandManager.register(new TestMenuCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, commandManager);
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PingListener(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new MainEventListener(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new CustomEventListener(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new HandShakeListener(this));
        long b = System.currentTimeMillis() - a;
        this.logHelper.info("&bAkane&fField &b已成功加载! &b耗时 &f" + b + "&bms");

        // bStats
        int serviceId = 17909;
        new Metrics(this, serviceId);
    }

    public void onDisable() {
        long a = System.currentTimeMillis();
        this.logHelper.info("&b卸载中..");
        this.isRunning = false;
        this.attackTrackerService.unload();
        this.firewallService.shutDownFirewall();
        this.userDataService.unload();
        this.VPNService.unload();
        this.antiBotManager.getBlackListService().unload();
        this.antiBotManager.getWhitelistService().unload();
        long b = System.currentTimeMillis() - a;
        this.logHelper.info("&b已成功卸载 耗时&f" + b + "&bms");
    }

    @Override
    public void reload() {
        this.config = new Config(this, "%datafolder%/config.yml");
        this.messages = new Config(this, "%datafolder%/messages.yml");

        ConfigManager.init(config);
        MessageManager.init(messages);
    }

    @Override
    public void runTask(Runnable task, boolean isAsync) {
        if (isAsync) {
            this.scheduler.runAsync(this, task);
        } else {
            this.scheduler.schedule(this, task, 0L, TimeUnit.SECONDS);
        }
    }

    @Override
    public void runTask(FieldRunnable runnable) {
        ScheduledTask task = null;

        if (runnable.isAsync()) {
            task = this.scheduler.runAsync(this, runnable);
        } else {
            task = this.scheduler.schedule(this, runnable, 0L, TimeUnit.SECONDS);
        }

        runnable.setTaskID(task.getId());
    }

    @Override
    public void scheduleDelayedTask(Runnable runnable, boolean async, long milliseconds) {
        if (async) {
            this.scheduler.schedule(this, () -> this.scheduler.runAsync(this, runnable), milliseconds,
                    TimeUnit.MILLISECONDS);
        } else {
            this.scheduler.schedule(this, runnable, milliseconds, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void scheduleDelayedTask(FieldRunnable runnable) {
        ScheduledTask task = null;

        if (runnable.isAsync()) {
            task = this.scheduler.schedule(this, () -> this.scheduler.runAsync(this, runnable), runnable.getTaskID(),
                    TimeUnit.MILLISECONDS);
        } else {
            task = this.scheduler.schedule(this, runnable, runnable.getPeriod(), TimeUnit.MILLISECONDS);
        }

        runnable.setTaskID(task.getId());
    }

    @Override
    public void scheduleRepeatingTask(Runnable runnable, boolean async, long repeatMilliseconds) {
        if (async) {
            this.scheduler.schedule(this, () -> this.scheduler.runAsync(this, runnable), 0L, repeatMilliseconds,
                    TimeUnit.MILLISECONDS);
        } else {
            this.scheduler.schedule(this, runnable, 0L, repeatMilliseconds, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void scheduleRepeatingTask(FieldRunnable runnable) {
        ScheduledTask task = null;

        if (runnable.isAsync()) {
            task = this.scheduler.schedule(this, () -> this.scheduler.runAsync(this, runnable), 0L,
                    runnable.getPeriod(), TimeUnit.MILLISECONDS);
        } else {
            task = this.scheduler.schedule(this, runnable, 0L, runnable.getPeriod(), TimeUnit.MILLISECONDS);
        }

        runnable.setTaskID(task.getId());
    }

    @Override
    public IConfiguration getConfigYml() {
        return this.config;
    }

    @Override
    public IConfiguration getMessages() {
        return this.messages;
    }

    @Override
    public IConfiguration getWhitelist() {
        return this.whitelist;
    }

    @Override
    public IConfiguration getBlackList() {
        return this.blacklist;
    }

    @Override
    public IAntiBotManager getAntiBotManager() {
        return this.antiBotManager;
    }

    @Override
    public LatencyThread getLatencyThread() {
        return this.latencyThread;
    }

    @Override
    public LogHelper getLogHelper() {
        return this.logHelper;
    }

    @Override
    public Class<?> getClassInstance() {
        return ProxyServer.getInstance().getClass();
    }

    @Override
    public UserDataService getUserDataService() {
        return this.userDataService;
    }

    @Override
    public VPNService getVPNService() {
        return this.VPNService;
    }

    @Override
    public INotificator getNotificator() {
        return this.notificator;
    }

    @Override
    public AkaneFieldCore getCore() {
        return this.core;
    }

    @Override
    public FirewallService getFirewallService() {
        return firewallService;
    }

    @Override
    public boolean isConnected(String ip) {
        List<String> ips = new ArrayList<>();
        ProxyServer.getInstance().getPlayers().forEach(a -> ips.add(Utils.getIP(a)));
        return ips.contains(ip);
    }

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public void disconnect(String ip, String reasonNoColor) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (Utils.getIP(player).equals(ip))
                player.disconnect(new TextComponent(ServerUtil.colorize(reasonNoColor)));
        }
    }

    @Override
    public int getOnlineCount() {
        return ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public void cancelTask(int id) {
        ProxyServer.getInstance().getScheduler().cancel(id);
    }

    @Override
    public void log(LogHelper.LogType type, String log) {
        switch (type) {
            case ERROR:
                ProxyServer.getInstance().getLogger().severe(log);
                break;
            case WARNING:
                ProxyServer.getInstance().getLogger().warning(log);
                break;
            case INFO:
                ProxyServer.getInstance().getLogger().info(log);
                break;
        }
    }

    @Override
    public void broadcast(String message) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent(ServerUtil.colorize(message)));
        }
    }

    @Override
    public AttackTrackerService getAttackTrackerService() {
        return attackTrackerService;
    }

    @Override
    public File getDFolder() {
        return getDataFolder();
    }

    public static AkaneFieldProxy getInstance() {
        return instance;
    }
}
