package catmoe.fallencrystal.akanefield.common.utils;

import java.util.concurrent.TimeUnit;

import catmoe.fallencrystal.akanefield.common.objects.FancyLong;

public class MathUtil {
    public static long convertToSeconds(long milliseconds) {
        return TimeUnit.MILLISECONDS.toSeconds(milliseconds);
    }

    public static long pastedSeconds(long milliseconds) {
        return convertToSeconds(System.currentTimeMillis() - milliseconds);
    }

    public static long pastedMillis(long milliseconds) {
        return System.currentTimeMillis() - milliseconds;
    }

    public static FancyLong pastedFancyMillis(long milliseconds) {
        return new FancyLong(pastedMillis(milliseconds));
    }

    public static FancyLong max(FancyLong a, FancyLong b) {
        return new FancyLong(Math.max(a.get(), b.get()));
    }

    public static FancyLong min(FancyLong a, FancyLong b) {
        return new FancyLong(Math.min(a.get(), b.get()));
    }
}
