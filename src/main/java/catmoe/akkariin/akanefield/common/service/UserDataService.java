package catmoe.akkariin.akanefield.common.service;

import catmoe.akkariin.akanefield.common.helper.LogHelper;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.IService;
import catmoe.akkariin.akanefield.common.objects.profile.ConnectionProfile;
import catmoe.akkariin.akanefield.common.utils.FileUtil;
import catmoe.akkariin.akanefield.common.utils.SerializeUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDataService implements IService {
    private final LogHelper logHelper;
    private List<String> firstJoin;
    private List<ConnectionProfile> connectionProfiles;

    public UserDataService(IAntiBotPlugin plugin) {
        this.logHelper = plugin.getLogHelper();
        this.firstJoin = new ArrayList<>();
        this.connectionProfiles = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load() {
        try {
            String encodedUsers = FileUtil.getEncodedBase64("users.dat", FileUtil.AkaneFieldFolder.DATA);
            String encodedConnections = FileUtil.getEncodedBase64("profiles.dat", FileUtil.AkaneFieldFolder.DATA);
            if (encodedUsers == null && encodedConnections == null) {
                return;
            }
            if (encodedUsers != null) {
                firstJoin = SerializeUtil.deserialize(encodedUsers, ArrayList.class);
                if (firstJoin == null)
                    firstJoin = new ArrayList<>();
            }
            if (encodedConnections != null) {
                connectionProfiles = SerializeUtil.deserialize(encodedConnections, ArrayList.class);
                if (connectionProfiles == null)
                    connectionProfiles = new ArrayList<>();
            }

            // remove olds
            connectionProfiles.removeIf(c -> c.getDaysFromLastJoin() >= 30);
        } catch (Exception e) {
            if (firstJoin == null)
                firstJoin = new ArrayList<>();
            if (connectionProfiles == null)
                connectionProfiles = new ArrayList<>();
            logHelper.error("Unable to load serialized files! If error persists contact support please!");
        }
    }

    @Override
    public void unload() {
        connectionProfiles.removeIf(c -> c.getDaysFromLastJoin() >= 30);
        FileUtil.writeBase64("users.dat", FileUtil.AkaneFieldFolder.DATA, firstJoin);
        FileUtil.writeBase64("profiles.dat", FileUtil.AkaneFieldFolder.DATA, connectionProfiles);
    }

    public void registerJoin(String ip, String nickname) {
        boolean state = false;
        for (ConnectionProfile profile : connectionProfiles) {
            if (profile.getIP().equals(ip)) {
                profile.onJoin(nickname);
                state = true;
                break;
            }
        }

        if (!state)
            connectionProfiles.add(new ConnectionProfile(ip, nickname));
    }

    public void registerQuit(String ip) {
        for (ConnectionProfile profile : connectionProfiles) {
            if (profile.getIP().equals(ip)) {
                profile.onDisconnect();
                break;
            }
        }
    }

    public void resetFirstJoin(String ip) {
        firstJoin.remove(ip);
    }

    public boolean isFirstJoin(String ip) {
        boolean b = !firstJoin.contains(ip);
        if (b)
            firstJoin.add(ip);
        return b;
    }

    public int size() {
        return firstJoin.size();
    }

    public List<String> getFirstJoins() {
        return firstJoin;
    }

    public List<ConnectionProfile> getConnectionProfiles() {
        return connectionProfiles;
    }
}
