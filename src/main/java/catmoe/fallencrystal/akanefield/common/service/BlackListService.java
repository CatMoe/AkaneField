package catmoe.fallencrystal.akanefield.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.IConfiguration;
import catmoe.fallencrystal.akanefield.common.IService;
import catmoe.fallencrystal.akanefield.common.helper.LogHelper;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListProfile;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListReason;

public class BlackListService implements IService {

    private final FirewallService firewallService;
    private final QueueService queueService;
    private final Map<String, BlackListProfile> blacklist;
    private final IConfiguration blacklistConfig;
    private final LogHelper logHelper;

    /**
     *
     * @param blacklistConfig - IConfiguration for BlackList Service
     * @param logHelper       - LogHelper for debug
     */
    public BlackListService(IAntiBotPlugin plugin, QueueService queueService, IConfiguration blacklistConfig,
            LogHelper logHelper) {
        this.firewallService = plugin.getFirewallService();
        this.queueService = queueService;
        this.blacklist = new HashMap<>();
        this.blacklistConfig = blacklistConfig;
        this.logHelper = logHelper;
    }

    /**
     * Adapt / DeAdapt IP Strings To File Saving...
     */

    private String toFileString(String str) {
        return str.replace(".", ",");
    }

    private String toIp(String str) {
        return str.replace(",", ".");
    }

    /**
     * Loading Configuration From File
     */
    @Override
    public void load() {
        for (String a : blacklistConfig.getConfigurationSection("data")) {
            String ip = toIp(a);
            String reason = blacklistConfig.getString("data." + a + ".reason");
            String id = blacklistConfig.getString("data." + a + ".id");
            String name = blacklistConfig.getString("data." + a + ".name");
            blacklist.put(ip, new BlackListProfile(ip, reason, id, name));
        }
        logHelper.info("&b???blacklist.yml????????? &b" + blacklist.size() + " ???IP. ?????????????????????...");
    }

    @Override
    public void unload() {
        blacklistConfig.set("data", null);
        for (Map.Entry<String, BlackListProfile> map : new HashMap<>(blacklist).entrySet()) {
            try {
                String ip = toFileString(map.getKey());
                String reason = map.getValue().getReason();
                String id = map.getValue().getId();
                blacklistConfig.set("data." + ip + ".reason", reason);
                blacklistConfig.set("data." + ip + ".id", id);
                blacklistConfig.set("data." + ip + ".name", map.getValue().getName());
            } catch (Exception e) {
                logHelper.error("An error occurred while saving blacklist.yml -> " + e.getMessage());
            }
        }
        blacklistConfig.save();
    }

    public void save() {
        blacklistConfig.set("data", null);
        for (Map.Entry<String, BlackListProfile> map : new HashMap<>(blacklist).entrySet()) {
            try {
                String ip = toFileString(map.getKey());
                String reason = map.getValue().getReason();
                String id = map.getValue().getId();
                blacklistConfig.set("data." + ip + ".reason", reason);
                blacklistConfig.set("data." + ip + ".id", id);
                blacklistConfig.set("data." + ip + ".name", map.getValue().getName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        blacklistConfig.save();
    }

    public List<String> getBlackListedIPS() {
        List<String> a = new ArrayList<>();
        new HashMap<>(blacklist).forEach((key, value) -> a.add(key));
        return a;
    }

    public int size() {
        return blacklist.size();
    }

    /**
     *
     * @param ip     The IP to BlackList
     * @param reason The Reason for BlackList
     * @param name   The name of the player
     */
    public void blacklist(String ip, BlackListReason reason, String name) {
        if (blacklist.containsKey(ip)) {
            return;
        }
        blacklist.put(ip, new BlackListProfile(ip, reason.getReason(), name));
        queueService.removeQueue(ip);
        firewallService.firewall(ip);
    }

    /**
     *
     * @param ip     The IP to BlackList
     * @param reason The Reason for BlackList
     */
    public void blacklist(String ip, BlackListReason reason) {
        if (blacklist.containsKey(ip)) {
            return;
        }
        blacklist.put(ip, new BlackListProfile(ip, reason.getReason()));
        queueService.removeQueue(ip);
        firewallService.firewall(ip);
    }

    /**
     *
     * @param ip     The IP to BlackList
     * @param reason The Reason for BlackList
     */
    public BlackListProfile blacklistAndGet(String ip, BlackListReason reason, String name) {
        if (blacklist.containsKey(ip)) {
            return getProfile(ip);
        }
        blacklist.put(ip, new BlackListProfile(ip, reason.getReason(), name));
        queueService.removeQueue(ip);
        firewallService.firewall(ip);
        return getProfile(ip);
    }

    public void clear() {
        blacklist.clear();
        firewallService.drop();
    }

    public void unBlacklist(String ip) {
        blacklist.remove(ip);
        firewallService.dropIP(ip);
    }

    public boolean isBlackListed(String ip) {
        return blacklist.containsKey(ip);
    }

    public BlackListProfile getProfile(String ip) {
        return blacklist.get(ip);
    }

    /**
     * @param id The Blacklist ID
     * @return Ip of the Player
     */
    public String getIPFromID(String id) {
        for (Map.Entry<String, BlackListProfile> map : blacklist.entrySet()) {
            if (map.getValue().getId().equals(id)) {
                return map.getKey();
            }
        }
        return null;
    }

    /**
     * @param id The Blacklist ID
     * @return BlackListProfile of the Player
     */
    public BlackListProfile getBlacklistProfileFromID(String id) {
        for (Map.Entry<String, BlackListProfile> map : blacklist.entrySet()) {
            if (map.getValue().getId().equals(id)) {
                return map.getValue();
            }
        }
        return null;
    }
}
