package catmoe.fallencrystal.akanefield.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.IService;
import catmoe.fallencrystal.akanefield.common.objects.attack.AttackLog;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.DateUtil;
import catmoe.fallencrystal.akanefield.common.utils.FileUtil;
import catmoe.fallencrystal.akanefield.common.utils.SerializeUtil;

public class AttackTrackerService implements IService {
    private final IAntiBotPlugin plugin;
    private List<AttackLog> attackLogList = new ArrayList<>();
    private AttackLog current;
    private int nextAttackID;

    public AttackTrackerService(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.current = null;
        this.nextAttackID = 0;
    }

    @Override
    public void load() {
        List<File> logFiles = Arrays.stream(FileUtil.getFiles(FileUtil.AkaneFieldFolder.LOGS))
                .filter(s -> s.getName().contains(".log")).collect(Collectors.toList());
        String nextAttackID = FileUtil.getEncodedBase64("attack.id", FileUtil.AkaneFieldFolder.LOGS);
        try {
            this.nextAttackID = Integer.parseInt(nextAttackID);
        } catch (NumberFormatException e) {
            this.nextAttackID = 0;
        }

        try {
            // case no files
            if (logFiles.size() == 0) {
                attackLogList = new ArrayList<>();
                return;
            }

            // with files
            for (File file : logFiles) {
                try {
                    String encoded = FileUtil.getEncodedBase64(file);
                    AttackLog log = SerializeUtil.deserialize(encoded, AttackLog.class);
                    if (log == null)
                        continue;
                    attackLogList.add(log);
                } catch (Exception e) {
                    file.delete();
                    plugin.getLogHelper().error("Unable to deserialize " + file.getName() + ", skipping...");
                }
            }

            // remove logs older than 30 days
            if (!ConfigManager.getAutoPurgerBoolean("logs.enabled"))
                return;
            attackLogList.removeIf(log -> {
                long pastedDays = TimeUnit.MICROSECONDS.toDays(log.getStopMillis());
                return pastedDays > ConfigManager.getAutoPurgerValue("logs.value");
            });
        } catch (Exception e) {
            if (attackLogList == null)
                attackLogList = new ArrayList<>();
            plugin.getLogHelper().error("Unable to load attacklogs files! If error persists contact support!");
        }
    }

    @Override
    public void unload() {
        for (AttackLog log : attackLogList) {
            try {
                FileUtil.writeBase64("attack-" + log.getID() + ".log", FileUtil.AkaneFieldFolder.LOGS, log);
            } catch (Exception e) {
                plugin.getLogHelper().warn("Unable to serialize attack-" + log.getID() + ".log (reason: "
                        + e.getMessage() + "), skipping...");
            }
        }
        FileUtil.writeLine("attack.id", FileUtil.AkaneFieldFolder.LOGS, String.valueOf(nextAttackID));
    }

    public List<AttackLog> getLastAttacks(int amount) {
        int size = attackLogList.size();
        List<AttackLog> result;

        if (size > amount) {
            result = attackLogList.subList(attackLogList.size() - amount, attackLogList.size());
        } else {
            result = attackLogList.subList(0, attackLogList.size());
        }

        return result;
    }

    public Optional<AttackLog> getAttackLog(int id) {
        return attackLogList.stream().filter(s -> s.getID() == id).findFirst();
    }

    public void onNewAttackStart() {
        if (current != null)
            return;
        nextAttackID++;
        current = new AttackLog(nextAttackID, DateUtil.getFullDateAndTime());
        current.recordStart(plugin.getAntiBotManager().getBlackListService().size(), plugin.getAntiBotManager());
    }

    public void onAttackStop() {
        if (current == null)
            return;
        current.recordStop(plugin.getAntiBotManager().getBlackListService().size(), plugin.getAntiBotManager());
        attackLogList.add(current);
        current = null;
    }
}
