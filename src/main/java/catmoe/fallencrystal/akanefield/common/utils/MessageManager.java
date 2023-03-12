package catmoe.fallencrystal.akanefield.common.utils;

import java.util.List;

import catmoe.fallencrystal.akanefield.common.IConfiguration;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListProfile;

public class MessageManager {
    public static IConfiguration configManger;

    public static double version;
    public static String prefix;
    public static String reloadMessage;
    public static String normalPingInterface;
    public static String verifiedPingInterface;
    public static String commandNoPerms;
    public static String blackwhiteListCleared;
    public static String commandPlayerOnly;

    public static String actionbarIdle;
    public static String actionbarBlock;
    public static String actionbarFirewall;
    public static String actionbarVerify;
    public static String actionbarMotd;
    public static String actionbarCombined;
    public static String toggledActionbar;

    public static List<String> helpMessage;
    public static List<String> statsMessage;

    private static String antiBotModeMessage;
    public static String firstJoinMessage;
    private static String accountOnlineMessage;
    private static String pingMessage;
    private static String timerMessage;
    private static String blacklistedMessage;
    private static String alreadyOnlineMessage;

    public static String reasonTooManyNicks;
    public static String reasonTooManyJoins;
    public static String reasonTooManyPings;
    public static String reasonStrangePlayer;
    public static String reasonCheck;
    public static String reasonVPN;
    public static String reasonAdmin;
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

    public static List<String> WhiteBlacklistConflectTipsMessages;
    public static boolean WhiteBlacklistConflectTipsEnabled;

    public static boolean attackNotificationTitleEnabled;
    public static String attackNotificationTitleTitle;
    public static String attackNotificationTitlesubTitle;
    public static int attackNotificationTitleFadeIn;
    public static int attackNotificationTitleStay;
    public static int attackNotificationTitleFadeOut;
    public static boolean attackNotificationChatEnabled;
    public static String attackNotificationChatMessages;

    public static void init(IConfiguration messages) {
        configManger = messages;
        version = messages.getDouble("version");
        prefix = messages.getString("prefix");
        reloadMessage = messages.getString("commands.reload");
        normalPingInterface = messages.getString("onping.normal");
        verifiedPingInterface = messages.getString("onping.ready");
        commandNoPerms = messages.getString("commands.perms");
        commandPlayerOnly = messages.getString("commands.player-only");

        actionbarIdle = messages.getString("notifications.actionbar.messages.idle");
        actionbarBlock = messages.getString("notifications.actionbar.messages.block");
        actionbarFirewall = messages.getString("notifications.actionbar.messages.firewall");
        actionbarVerify = messages.getString("notifications.actionbar.messages.verify");
        actionbarMotd = messages.getString("notifications.actionbar.messages.idle");
        actionbarCombined = messages.getString("notifications.actionbar.messages.combined");
        toggledActionbar = messages.getString("notifications.actionbar.toggle");

        helpMessage = messages.getStringList("help");
        statsMessage = messages.getStringList("stats");

        antiBotModeMessage = convertToString(messages.getStringList("kick.antibotmode"));
        firstJoinMessage = convertToString(messages.getStringList("kick.first-join"));
        accountOnlineMessage = convertToString(messages.getStringList("kick.account-online"));
        pingMessage = convertToString(messages.getStringList("kick.ping"));
        timerMessage = convertToString(messages.getStringList("kick.timer"));
        blacklistedMessage = convertToString(messages.getStringList("kick.blacklisted"));
        alreadyOnlineMessage = convertToString(messages.getStringList("kick.already-online"));

        reasonTooManyNicks = messages.getString("reason.names");
        reasonTooManyJoins = messages.getString("reason.joins");
        reasonTooManyPings = messages.getString("reason.pings");
        reasonStrangePlayer = messages.getString("reason.strange");
        reasonCheck = messages.getString("reason.check");
        reasonVPN = messages.getString("reason.vpn");
        reasonAdmin = messages.getString("reason.admin");

        commandWrongArgument = messages.getString("commands.wrong-args");
        reasonBlacklistAdmin = messages.getString("reason.admin");
        commandNoBlacklist = messages.getString("white-black-list.cleared");
        blacklistProfileString = messages.getStringList("blacklist_info");
        firewallMessage = messages.getStringList("firewall");
        fastJoinQueueMessage = convertToString(messages.getStringList("fastjoin-queue"));
        satelliteStatus = messages.getStringList("satellitestats");

        blackwhiteListCleared = messages.getString("white-black-list.cleared");

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

        WhiteBlacklistConflectTipsMessages = messages.getStringList("white-black-list.conflect-tips.messages");
        WhiteBlacklistConflectTipsEnabled = messages.getBoolean("white-black-list.conflect-tips.enabled");

        attackNotificationTitleEnabled = messages.getBoolean("notifications.underattack.title.title");
        attackNotificationTitleTitle = messages.getString("notifications.underattack.title.title");
        attackNotificationTitlesubTitle = messages.getString("notifications.underattack.title.subtitle");
        attackNotificationTitleFadeIn = messages.getInt("notifications.underattack.title.fade-in");
        attackNotificationTitleStay = messages.getInt("notifications.underattack.title.stay");
        attackNotificationTitleFadeOut = messages.getInt("notifications.underattack.title.fade-out");
        attackNotificationChatEnabled = messages.getBoolean("notifications.underattack.chat.enabled");
        attackNotificationChatMessages = messages.getString("notifications.underattack.chat.message");
    }

    public static String getCommandNoPerms() {
        return commandNoPerms;
    }

    public static String getBlackWhiteListCleared(String what) {
        return blackwhiteListCleared.replace("%type%", what);
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

    public static String getAlreadyOnlineMessage() {
        return alreadyOnlineMessage.replace("[$URL]", kickURL).replace("[$ServerName]", kickServerName)
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
