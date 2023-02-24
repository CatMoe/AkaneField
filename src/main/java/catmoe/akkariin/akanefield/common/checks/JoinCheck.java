package catmoe.akkariin.akanefield.common.checks;

public interface JoinCheck extends Check {
    boolean isDenied(String ip, String name);
}
