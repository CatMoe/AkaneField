package catmoe.akkariin.akanefield.common.checks;

public interface ChatCheck extends Check {
    void onChat(String ip, String nickname, String message);

    void onTabComplete(String ip, String nickname, String message);
}
