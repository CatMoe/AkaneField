package catmoe.fallencrystal.akanefield.task;

import catmoe.fallencrystal.akanefield.common.AttackState;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.ModeType;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.event.AttackStateEvent;
import catmoe.fallencrystal.akanefield.utils.EventCaller;

public class ModeDisableTask implements Runnable {

    private final IAntiBotPlugin antiBotPlugin;
    private final ModeType disableMode;

    public ModeDisableTask(IAntiBotPlugin antiBotPlugin, ModeType disableMode) {
        this.antiBotPlugin = antiBotPlugin;
        this.disableMode = disableMode;
    }

    @Override
    public void run() {
        if (antiBotPlugin.getAntiBotManager().canDisable(disableMode)) {
            EventCaller.call(new AttackStateEvent(antiBotPlugin, AttackState.STOPPED, disableMode));
            antiBotPlugin.getAntiBotManager().disableMode(disableMode);
            return;
        }
        antiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(antiBotPlugin, disableMode),
                false,
                1000L * ConfigManager.antiBotModeKeep);
        EventCaller.call(new AttackStateEvent(antiBotPlugin, AttackState.RUNNING, disableMode));
    }
}
