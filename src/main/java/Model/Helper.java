package Model;

import java.util.Arrays;
import java.util.List;

public abstract class Helper {
    public static boolean listContainsArray(List<int[]> list, int[] array) {
        for (int[] x : list) {
            if (Arrays.equals(x, array)) {
                return true;
            }
        }
        return false;
    }
}
