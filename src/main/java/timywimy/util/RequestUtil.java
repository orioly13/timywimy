package timywimy.util;

import java.util.Random;

public class RequestUtil {

    private static final int LOWER_BOUND = 1000000;
    private static final int UPPER_BOUND = 10000000;
    private static final Random RANDOM = new Random();

    private RequestUtil() {
    }

    public static int getRandomRequestId() {
        return RANDOM.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }
}
