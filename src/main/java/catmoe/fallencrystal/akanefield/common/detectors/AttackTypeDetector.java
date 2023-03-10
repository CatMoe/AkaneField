package catmoe.fallencrystal.akanefield.common.detectors;

//import catmoe.fallencrystal.akanefield.common.AttackType;
//import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
//import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
//import catmoe.fallencrystal.akanefield.common.ModeType;

/**
 * Tagged as to be removed in the future
 */
@Deprecated
public class AttackTypeDetector extends AbstractDetector {
    @Override
    public int getTickDelay() {
        return 0;
    }

    @Override
    public void tick() {

    }
    /*
     * private final IAntiBotManager manager;
     * 
     * public AttackTypeDetector(IAntiBotPlugin plugin){
     * this.manager = plugin.getAntiBotManager();
     * }
     * 
     * @Override
     * public int getTickDelay() {
     * return 2;
     * }
     * 
     * @Override
     * public void tick() {
     * ModeType current = manager.getModeType();
     * 
     * if(current.equals(ModeType.SlowJoin)) {
     * manager.setAttackType(AttackType.SlowJoin);
     * return;
     * }
     * if(current.equals(ModeType.Motd)){
     * manager.setAttackType(AttackType.MOTD);
     * return;
     * }
     * if(manager.isPacketModeEnabled()){
     * manager.setAttackType(AttackType.Firewall);
     * return;
     * }
     * 
     * if(!manager.isAntiBotModeEnabled()) return;
     * 
     * long cps = manager.getJoinPerSecond();
     * long pps = manager.getPingPerSecond();
     * 
     * long min = cps * 15 / 100;
     * 
     * if(pps >= min && cps > 10){
     * manager.setAttackType(AttackType.COMBINED);
     * return;
     * }
     * 
     * manager.setAttackType(AttackType.JOIN);
     * }
     */
}
