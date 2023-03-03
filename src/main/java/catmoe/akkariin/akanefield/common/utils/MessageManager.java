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
    public static String commandPlayerOnly;
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
    public static List<String> firewallMessage;
    public static String bossBarBlockMessage;
    public static String fastJoinQueueMessage;
    public static List<String> satelliteStatus;
    public static String LogInvalidValue;
    public static String LogisEmpty;

    public static String kickURL;
    public static String kickServerName;
    public static String kickLine;
    public static String kickCustom1;
    public static String kickCustom2;
    public static String kickCustom3;
    public static String kickCustom4;
    public static String kickCustom5;
    public static String kickCustom6;
    public static String kickCustom7;
    public static String kickCustom8;
    public static String kickCustom9;
    public static String kickCustom10;

    public static boolean attackNotificationTitleEnabled;
    public static String attackNotificationTitleTitle;
    public static String attackNotificationTitlesubTitle;
    public static int attackNotificationTitleFadeIn;
    public static int attackNotificationTitleStay;
    public static int attackNotificationTitleFadeOut;
    public static boolean attackNotificationChatEnabled;
    public static List<String> attackNotificationChatMessages;

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
        commandPlayerOnly = messages.getString("commands.player-only");
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
        commandWrongArgument = messages.getString("commands.wrong-args");
        reasonBlacklistAdmin = messages.getString("reason.admin");
        commandNoBlacklist = messages.getString("commands.no-blacklist");
        blacklistProfileString = messages.getStringList("blacklist_info");
        firewallMessage = messages.getStringList("firewall");
        fastJoinQueueMessage = convertToString(messages.getStringList("fastjoin-queue"));
        satelliteStatus = messages.getStringList("satellitestats");

        LogInvalidValue = messages.getString("log.invalid-value");
        LogisEmpty = messages.getString("log.empty");

        kickURL = messages.getString("kick.url");
        kickServerName = messages.getString("kick.servername");
        kickLine = messages.getString("kick.line");
        kickCustom1 = messages.getString("kick.custom1");
        kickCustom2 = messages.getString("kick.custom2");
        kickCustom3 = messages.getString("kick.custom3");
        kickCustom4 = messages.getString("kick.custom4");
        kickCustom5 = messages.getString("kick.custom5");
        kickCustom6 = messages.getString("kick.custom6");
        kickCustom7 = messages.getString("kick.custom7");
        kickCustom8 = messages.getString("kick.custom8");
        kickCustom9 = messages.getString("kick.custom9");
        kickCustom10 = messages.getString("kickcustom10");

        attackNotificationTitleEnabled = messages.getBoolean("underattack.title.title");
        attackNotificationTitleTitle = messages.getString("underattack.title.title");
        attackNotificationTitlesubTitle = messages.getString("underattack.title.subtitle");
        attackNotificationTitleFadeIn = messages.getInt("underattack.title.fade-in");
        attackNotificationTitleStay = messages.getInt("underattack.title.stay");
        attackNotificationTitleFadeOut = messages.getInt("underattack.title.fade-out");
        attackNotificationChatEnabled = messages.getBoolean("underattack.chat.enabled");
        attackNotificationChatMessages = messages.getStringList("underattack.chat.messages");
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

    // 为了可自定义性的占位符做有必要的牺牲! (?)
    public static String getAntiBotModeMessage(String checkPercent, String blackListPercentage) {
        return antiBotModeMessage
                .replace("[$blacklist]", blackListPercentage).replace("[$queue]", checkPercent)
                .replace("[$URL]", kickURL).replace("[$ServerName]", kickServerName).replace("[$Line]", kickLine)
                .replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
    }

    public static String getFirstJoinMessage(String nothing) {
        return firstJoinMessage
                .replace("[$URL]", kickURL).replace("[$ServerName]", kickServerName)
                .replace("[$Line]", kickLine).replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
    }

    public static String getAccountOnlineMessage() {
        return accountOnlineMessage
                .replace("[$URL]", kickURL).replace("[$ServerName]", kickServerName)
                .replace("[$Line]", kickLine).replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
    }

    public static String getPingMessage(String times) {
        return pingMessage
                .replace("[$pingcount]", times).replace("[$URL]", kickURL)
                .replace("[$ServerName]", kickServerName)
                .replace("[$Line]", kickLine).replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
    }

    public static String getTimerMessage(String times) {
        return timerMessage
                .replace("[$delay]", times).replace("[$URL]", kickURL)
                .replace("[$ServerName]", kickServerName)
                .replace("[$Line]", kickLine).replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
    }

    public static String getBlacklistedMessage(BlackListProfile profile) {
        return blacklistedMessage
                .replace("[$id]", profile.getId()).replace("[$reason]", profile.getReason())
                .replace("[$URL]", kickURL).replace("[$ServerName]", kickServerName)
                .replace("[$Line]", kickLine).replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
    }

    public static String getFastJoinQueueMessage(String nothing) {
        return fastJoinQueueMessage
                .replace("[$URL]", kickURL).replace("[$ServerName]", kickServerName)
                .replace("[$Line]", kickLine).replace("[$Custom1]", kickCustom1).replace("[$Custom2]", kickCustom2)
                .replace("[$Custom3]", kickCustom3).replace("[$Custom4]", kickCustom4)
                .replace("[$Custom5]", kickCustom5).replace("[$Custom6]", kickCustom6)
                .replace("[$Custom7]", kickCustom7).replace("[$Custom8]", kickCustom8)
                .replace("[$Custom9]", kickCustom9).replace("[$Custom10]", kickCustom10);
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
