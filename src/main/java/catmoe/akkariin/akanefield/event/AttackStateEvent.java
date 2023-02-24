package catmoe.akkariin.akanefield.event;

import catmoe.akkariin.akanefield.common.AttackState;
import catmoe.akkariin.akanefield.common.ModeType;
import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import net.md_5.bungee.api.plugin.Event;

public class AttackStateEvent extends Event {
    private IAntiBotPlugin plugin;
    private IAntiBotManager antiBotManager;
    private AttackState attackState;
    private ModeType attackTYPE;

    public AttackStateEvent(IAntiBotPlugin plugin, AttackState attackState, ModeType attackTYPE) {
        this.plugin = plugin;
        this.antiBotManager = plugin.getAntiBotManager();
        this.attackState = attackState;
        this.attackTYPE = attackTYPE;
    }

    public ModeType getAttackTYPE() {
        return attackTYPE;
    }

    public void setAttackTYPE(ModeType attackTYPE) {
        this.attackTYPE = attackTYPE;
    }

    public IAntiBotPlugin getPlugin() {
        return plugin;
    }

    public IAntiBotManager getAntiBotManager() {
        return antiBotManager;
    }

    public AttackState getAttackState() {
        return attackState;
    }

    public void setPlugin(IAntiBotPlugin plugin) {
        this.plugin = plugin;
    }

    public void setAntiBotManager(IAntiBotManager antiBotManager) {
        this.antiBotManager = antiBotManager;
    }

    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }
}
