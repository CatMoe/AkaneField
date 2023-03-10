package catmoe.fallencrystal.akanefield.common;

import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;

public abstract class FieldRunnable implements Runnable {
    private int taskID;

    public abstract boolean isAsync();

    public abstract long getPeriod();

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void cancel() {
        ServerUtil.cancelTask(this);
    }
}
