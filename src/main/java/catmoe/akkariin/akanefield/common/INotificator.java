package catmoe.akkariin.akanefield.common;

public interface INotificator {

    void sendActionbar(String str);

    void sendTitle(String title, String subtitle);

    void init(IAntiBotPlugin plugin);
}
