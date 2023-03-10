package catmoe.fallencrystal.akanefield.common.checks;

import java.util.Queue;

import com.google.common.collect.EvictingQueue;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class NameSimilarityCheck implements JoinCheck {
    private final Queue<String> nameHistory = EvictingQueue.create(128);
    private final LevenshteinDistance distanceAlgorithm = LevenshteinDistance.getDefaultInstance();

    public NameSimilarityCheck(IAntiBotPlugin plugin) {
    }

    public boolean isDenied(String ip, String name) {
        if (!isEnabled())
            return false;
        synchronized (this.nameHistory) {
            for (String nick : this.nameHistory) {
                if (nick.equals(name)) {
                    return false;
                }
                int distance = this.distanceAlgorithm.apply(nick, name);
                if (distance <= 4) {
                    return true;
                }
            }
        }
        this.nameHistory.add(name);
        return false;
    }

    @Override
    public void onDisconnect(String ip, String name) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void loadTask() {
    }
}
