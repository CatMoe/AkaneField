package catmoe.akkariin.akanefield.common.antivpn;

public interface VPNProvider {
    String getID();

    void process(String ip, String name);

    String getCountry(String ip, String name);
}
