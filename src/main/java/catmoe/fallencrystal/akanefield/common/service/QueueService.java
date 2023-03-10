package catmoe.fallencrystal.akanefield.common.service;

import java.util.HashSet;
import java.util.Set;

import catmoe.fallencrystal.akanefield.common.IService;

public class QueueService implements IService {

    private final Set<String> queue;

    public QueueService() {
        this.queue = new HashSet<>();
    }

    @Override
    public void load() {
    }

    @Override
    public void unload() {
        queue.clear();
    }

    public boolean isQueued(String ip) {
        return queue.contains(ip);
    }

    public void queue(String ip) {
        queue.add(ip);
    }

    public void removeQueue(String ip) {
        queue.remove(ip);
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }
}
