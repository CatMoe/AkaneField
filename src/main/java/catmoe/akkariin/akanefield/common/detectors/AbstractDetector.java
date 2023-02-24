package catmoe.akkariin.akanefield.common.detectors;

import catmoe.akkariin.akanefield.common.service.DetectorService;

public abstract class AbstractDetector {

    public AbstractDetector() {
        DetectorService.register(this);
    }

    public abstract int getTickDelay();

    public abstract void tick();
}
