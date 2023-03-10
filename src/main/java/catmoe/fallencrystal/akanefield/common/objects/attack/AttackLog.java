package catmoe.fallencrystal.akanefield.common.objects.attack;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.utils.TimeUtil;

public class AttackLog implements Serializable {
    private final int ID;
    private final String attackDate;
    private long previousBlacklist, previousBots, previousPings, previousPackets = 0;
    private long newBlacklist = 0;
    private long blockedBots = 0;
    private long blockedPings = 0;
    private long blockedPackets = 0;
    private long startMillis;
    private long stopMillis = 0;
    private long attackDuration = 0;

    public AttackLog(int ID, String attackDate) {
        this.ID = ID;
        this.attackDate = attackDate;
    }

    public void recordStart(long currentBlacklist, IAntiBotManager manager) {
        previousBlacklist = currentBlacklist;
        previousBots = manager.getDynamicJoins().getTotal();
        previousPings = manager.getDynamicPings().getTotal();
        previousPackets = manager.getDynamicPackets().getTotal();
        this.startMillis = System.currentTimeMillis();
    }

    public void recordStop(long newBlacklist, IAntiBotManager manager) {
        this.newBlacklist = newBlacklist;
        blockedBots = manager.getDynamicJoins().getTotal() - previousBots;
        blockedPings = manager.getDynamicPings().getTotal() - previousPings;
        blockedPackets = manager.getDynamicPackets().getTotal() - previousPackets;
        this.stopMillis = System.currentTimeMillis();
        this.attackDuration = stopMillis - startMillis;
    }

    public int getID() {
        return ID;
    }

    public String getAttackDate() {
        return attackDate;
    }

    public long getPreviousBlacklist() {
        return previousBlacklist;
    }

    public long getNewBlacklist() {
        return newBlacklist;
    }

    public long getBlockedBots() {
        return blockedBots;
    }

    public long getBlockedPings() {
        return blockedPings;
    }

    public long getBlockedPackets() {
        return blockedPackets;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getStopMillis() {
        return stopMillis;
    }

    public long getAttackDuration() {
        return attackDuration;
    }

    public AttackPower getAttackPower() {
        long sum = getAverageConnections();
        if (sum > 80000) {
            return AttackPower.INCREDIBLE;
        }
        if (sum > 40000) {
            return AttackPower.HUGE;
        }
        if (sum > 25000) {
            return AttackPower.LARGE;
        }
        if (sum > 15000) {
            return AttackPower.HIGH;
        }
        if (sum > 5000) {
            return AttackPower.MEDIUM;
        }
        return AttackPower.LOW;
    }

    public long getAverageConnections() {
        long duration = TimeUnit.MILLISECONDS.toSeconds(attackDuration);
        long removal = 15 * duration / 100;
        duration -= removal;
        long total = blockedBots + blockedPings + blockedPackets;
        return total / duration;
    }

    public long getTotalBlocked() {
        long total = blockedBots + blockedPings + blockedPackets;
        return total;
    }

    public String replaceInformation(String message) {
        return message.replace("%avg%", String.valueOf(getAverageConnections()))
                .replace("%duration%", TimeUtil.formatMilliseconds(attackDuration))
                .replace("%totalblocked%", String.valueOf(getTotalBlocked()))
                .replace("%totalpackets%", String.valueOf(blockedPackets))
                .replace("%totalpings%", String.valueOf(getBlockedPings()))
                .replace("%totalbots%", String.valueOf(getBlockedBots()))
                .replace("%blacklist%", String.valueOf(newBlacklist - previousBlacklist))
                .replace("%date%", attackDate).replace("%id%", String.valueOf(getID()));
    }
}
