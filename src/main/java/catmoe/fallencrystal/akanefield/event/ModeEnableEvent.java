package catmoe.fallencrystal.akanefield.event;

import net.md_5.bungee.api.plugin.Event;

import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.ModeType;
import catmoe.fallencrystal.akanefield.common.service.UserDataService;

public class ModeEnableEvent extends Event {
    private final IAntiBotManager antiBotManager;
    private final ModeType enabledMode;
    private final UserDataService userDataService;

    public ModeEnableEvent(IAntiBotPlugin antiBotPlugin, ModeType modeType) {
        this.antiBotManager = antiBotPlugin.getAntiBotManager();
        this.enabledMode = modeType;
        this.userDataService = antiBotPlugin.getUserDataService();
    }

    public ModeType getEnabledMode() {
        return enabledMode;
    }

    public void disconnectBots() {
        List<String> profileList = new ArrayList<>(antiBotManager.getJoinCache().getJoined(10));
        profileList.forEach(userDataService::resetFirstJoin);
        antiBotManager.getJoinCache().clear();
    }
}
