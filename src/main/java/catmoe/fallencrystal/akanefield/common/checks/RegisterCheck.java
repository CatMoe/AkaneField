package catmoe.fallencrystal.akanefield.common.checks;

import java.util.*;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.objects.FancyInteger;
import catmoe.fallencrystal.akanefield.common.objects.LimitedList;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListReason;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

public class RegisterCheck implements ChatCheck {
    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antiBotManager;

    // password, times
    private final Map<String, FancyInteger> passwordScore;
    // ip, password
    private final Map<String, String> ipPasswordMap;
    // nickname, password
    private final Map<String, LimitedList<String>> nicknamePasswordMap;
    private final List<String> trackedCommands;

    public RegisterCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.antiBotManager = plugin.getAntiBotManager();
        this.passwordScore = new HashMap<>();
        this.ipPasswordMap = new HashMap<>();
        this.nicknamePasswordMap = new HashMap<>();

        this.trackedCommands = ConfigManager.registerCheckCommandListeners;
    }

    @Override
    public void onChat(String ip, String nickname, String message) {
        if (!isEnabled()) {
            return;
        }
        if (!isTrackedRegisterCommand(message)) {
            return;
        }
        // skip if whitelisted
        if (plugin.getAntiBotManager().getWhitelistService().isWhitelisted(ip))
            return;

        String[] split = message.split("\\s+");
        if (split.length < 1)
            return;

        String password = split[1];
        registerPassword(ip, nickname, password);

        // attack checking
        if (isLimitReached()) {
            for (String passwd : getOverLimitPasswords()) {
                for (String ipToBlock : getSamePasswordIPS(passwd)) {
                    if (ConfigManager.isRegisterCheckBlacklist) {
                        antiBotManager.getBlackListService().blacklist(ipToBlock, BlackListReason.STRANGE_PLAYER,
                                nickname);
                    }
                    if (ConfigManager.isRegisterCheckAntiBotMode) {
                        antiBotManager.enableSlowAntiBotMode();
                    }
                }
            }
            plugin.getLogHelper().debug("Detected attack on RegisterCheck!");
        }
    }

    @Override
    public void onTabComplete(String ip, String nickname, String message) {

    }

    @Override
    public void onDisconnect(String ip, String name) {
        ipPasswordMap.remove(ip);
        nicknamePasswordMap.remove(ip);
    }

    @Override
    public boolean isEnabled() {
        return ConfigManager.isRegisterCheckEnabled;
    }

    @Override
    public void loadTask() {
        plugin.scheduleRepeatingTask(() -> {
            ipPasswordMap.clear();
            passwordScore.clear();
            nicknamePasswordMap.clear();
        }, false, 1000L * ConfigManager.taskManagerClearRegister);
    }

    private boolean isTrackedRegisterCommand(String message) {
        for (String str : trackedCommands) {
            if (message.split("\\s+")[0].equalsIgnoreCase(str) && hasPassword(message))
                return true;
        }

        return false;
    }

    private boolean hasPassword(String message) {
        try {
            String[] arr = message.split("\\s+");
            return arr.length > 1;
        } catch (Exception e) {
            return false;
        }
    }

    private void registerPassword(String ip, String nickname, String password) {
        if (!nicknamePasswordMap.containsKey(nickname) && nicknamePasswordMap.get(nickname) == null) {
            nicknamePasswordMap.put(nickname, new LimitedList<String>(10).add(password));
            ipPasswordMap.put(ip, password);
            passwordScore.put(password, passwordScore.getOrDefault(password, new FancyInteger(0)).increase());
        }

        if (nicknamePasswordMap.get(nickname).matches(s -> s.equalsIgnoreCase(password))) {
            return;
        }

        nicknamePasswordMap.get(nickname).add(password);
        ipPasswordMap.put(ip, password);
        passwordScore.put(password, passwordScore.getOrDefault(password, new FancyInteger(0)).increase());
    }

    private List<String> getSamePasswordIPS(String password) {
        return ipPasswordMap.entrySet()
                .stream()
                .filter(s -> s.getValue().equalsIgnoreCase(password))
                .collect(ArrayList::new, (list, entry) -> list.add(entry.getKey()), (a, b) -> {
                });
    }

    private List<String> getOverLimitPasswords() {
        return passwordScore.entrySet()
                .stream()
                .filter(s -> s.getValue().get() >= ConfigManager.registerCheckLimit)
                .collect(ArrayList::new, (list, entry) -> list.add(entry.getKey()), (a, b) -> {
                });
    }

    private boolean isLimitReached() {
        return passwordScore.entrySet().stream().anyMatch(s -> s.getValue().get() >= ConfigManager.registerCheckLimit);
    }
}
