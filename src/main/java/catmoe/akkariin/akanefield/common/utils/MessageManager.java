package catmoe.akkariin.akanefield.common.utils;

import catmoe.akkariin.akanefield.common.IConfiguration;
import catmoe.akkariin.akanefield.common.objects.profile.BlackListProfile;

import java.util.List;

public class MessageManager {
    public static IConfiguration configManger;

    public static double version;
    public static String prefix;
    public static String reloadMessage;
    public static String normalPingInterface;
    public static String verifiedPingInterface;
    public static String commandNoPerms;
    public static String commandCleared;
    public static String commandAdded;
    public static String commandRemove;
    public static String actionbarOffline;
    public static String actionbarAntiBotMode;
    public static String actionbarPackets;
    public static List<String> helpMessage;
    public static List<String> statsMessage;
    private static String antiBotModeMessage;
    public static String firstJoinMessage;
    private static String accountOnlineMessage;
    private static String pingMessage;
    private static String timerMessage;
    private static String blacklistedMessage;
    public static String reasonTooManyNicks;
    public static String reasonTooManyJoins;
    public static String reasonTooManyPings;
    public static String reasonStrangePlayer;
    public static String reasonCheck;
    public static String reasonVPN;
    public static String reasonAdmin;
    public static String toggledActionbar;
    public static String toggledBossBar;
    public static String commandWrongArgument;
    public static String reasonBlacklistAdmin;
    public static String commandNoBlacklist;
    public static List<String> blacklistProfileString;
    public static String attackAnalyzerIncrease;
    public static List<String> firewallMessage;
    public static String attackAnalyzerDecrease;
    public static String bossBarIdleMessage;
    public static String fastJoinQueueMessage;
    public static List<String> satelliteStatus;

    public static void init(IConfiguration messages) {
        configManger = messages;
        version = messages.getDouble("version");
        prefix = messages.getString("prefix");
        reloadMessage = messages.getString("commands.reload");
        normalPingInterface = messages.getString("onping.normal");
        verifiedPingInterface = messages.getString("onping.ready");
        commandNoPerms = messages.getString("commands.perms");
        commandCleared = messages.getString("commands.cleared");
        commandAdded = messages.getString("commands.added");
        commandRemove = messages.getString("commands.removed");
        actionbarOffline = messages.getString("actionbar.offline");
        actionbarAntiBotMode = messages.getString("actionbar.antibot");
        actionbarPackets = messages.getString("actionbar.packets");
        helpMessage = messages.getStringList("help");
        statsMessage = messages.getStringList("stats");
        antiBotModeMessage = convertToString(messages.getStringList("antibotmode"));
        firstJoinMessage = convertToString(messages.getStringList("first_join"));
        accountOnlineMessage = convertToString(messages.getStringList("account-online"));
        pingMessage = convertToString(messages.getStringList("ping"));
        timerMessage = convertToString(messages.getStringList("timer"));
        blacklistedMessage = convertToString(messages.getStringList("blacklisted"));
        reasonTooManyNicks = messages.getString("reason.names");
        reasonTooManyJoins = messages.getString("reason.joins");
        reasonTooManyPings = messages.getString("reason.pings");
        reasonStrangePlayer = messages.getString("reason.strange");
        reasonCheck = messages.getString("reason.check");
        reasonVPN = messages.getString("reason.vpn");
        reasonAdmin = messages.getString("reason.admin");
        toggledActionbar = messages.getString("notifications.action");
        toggledBossBar = messages.getString("notifications.bossbar");
        commandWrongArgument = messages.getString("commands.wrong-args");
        reasonBlacklistAdmin = messages.getString("reason.admin");
        commandNoBlacklist = messages.getString("commands.no-blacklist");
        blacklistProfileString = messages.getStringList("blacklist_info");
        attackAnalyzerIncrease = messages.getString("analyzer.increase");
        firewallMessage = messages.getStringList("firewall");
        attackAnalyzerDecrease = messages.getString("analyzer.decrease");
        bossBarIdleMessage = messages.getString("bossbar_idle_message");
        fastJoinQueueMessage = convertToString(messages.getStringList("fastjoin-queue"));
        satelliteStatus = messages.getStringList("satellitestats");

    }

    public static String getCommandNoPerms() {
        return commandNoPerms;
    }

    public static String getCommandCleared(String what) {
        return commandCleared.replace("$1", what);
    }

    public static String getCommandAdded(String ip, String were) {
        return commandAdded.replace("$2", were).replace("$1", ip);
    }

    public static String getCommandRemove(String ip, String were) {
        return commandRemove.replace("$2", were).replace("$1", ip);
    }

    public static String getAntiBotModeMessage(String checkPercent, String blackListPercentage) {
        return antiBotModeMessage.replace("$2", blackListPercentage).replace("$1", checkPercent);
    }

    public static String getAccountOnlineMessage() {
        return accountOnlineMessage;
    }

    public static String getPingMessage(String times) {
        return pingMessage.replace("$1", times);
    }

    public static String getTimerMessage(String times) {
        return timerMessage.replace("$1", times);
    }

    public static String getBlacklistedMessage(BlackListProfile profile) {
        return blacklistedMessage.replace("$2", profile.getId()).replace("$1", profile.getReason());
    }

    private static String convertToString(List<String> stringList) {
        return String.join("\n", stringList);
    }

    public static String getMessage(String path) {
        return configManger.getString(path);
    }

    public static List<String> getMessageList(String path) {
        return configManger.getStringList(path);
    }
}
