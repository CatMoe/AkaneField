package catmoe.fallencrystal.akanefield.common.server.packet;

import java.io.DataOutputStream;

public interface SatellitePacket {

    String getID();

    void write(DataOutputStream output);
}
