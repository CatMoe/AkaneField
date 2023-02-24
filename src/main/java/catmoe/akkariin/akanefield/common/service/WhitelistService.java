package catmoe.akkariin.akanefield.common.service;

import catmoe.akkariin.akanefield.common.helper.LogHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import catmoe.akkariin.akanefield.common.IConfiguration;
import catmoe.akkariin.akanefield.common.IService;

public class WhitelistService implements IService {

    private final QueueService queueService;
    private final Set<String> whitelist;
    private final IConfiguration whitelistConfig;
    private final LogHelper logHelper;

    public WhitelistService(QueueService queueService, IConfiguration whitelistConfig, LogHelper logHelper) {
        this.queueService = queueService;
        this.whitelist = new HashSet<>();
        this.whitelistConfig = whitelistConfig;
        this.logHelper = logHelper;
    }

    @Override
    public void load() {
        try {
            whitelist.addAll(whitelistConfig.getStringList("data"));
            logHelper.info("&b从whitelist.yml中找到 &b" + whitelist.size() + " 个IP. 正在注册白名单...");
        } catch (Exception e) {
            logHelper.error("Error while loading whitelist...");
            e.printStackTrace();
        }
    }

    @Override
    public void unload() {
        try {
            whitelistConfig.set("data", new ArrayList<>(whitelist));
        } catch (Exception ignored) {
            logHelper.error("Error while saving whitelist...");
        }

        whitelistConfig.save();
    }

    public void save() {
        try {
            whitelistConfig.set("data", new ArrayList<>(whitelist));
        } catch (Exception ignored) {
            logHelper.error("Error while saving whitelist...");
        }

        whitelistConfig.save();
    }

    public int size() {
        return whitelist.size();
    }

    public void whitelist(String ip) {
        whitelist.add(ip);
        queueService.removeQueue(ip);
    }

    public void clear() {
        whitelist.clear();
    }

    public void unWhitelist(String ip) {
        whitelist.remove(ip);
    }

    public boolean isWhitelisted(String ip) {
        return whitelist.contains(ip);
    }

    public void whitelistAll(String... ip) {
        whitelist.addAll(Arrays.asList(ip));
    }

    public void removeAll(String... ip) {
        Arrays.asList(ip).forEach(whitelist::remove);
    }

    public Collection<String> getWhitelistedIPS() {
        return whitelist;
    }
}
