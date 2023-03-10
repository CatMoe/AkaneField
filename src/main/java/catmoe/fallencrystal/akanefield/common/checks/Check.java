package catmoe.fallencrystal.akanefield.common.checks;

public interface Check {
    void onDisconnect(String ip, String name);

    boolean isEnabled();

    void loadTask();
}
