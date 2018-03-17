package timywimy.util;

import timywimy.util.exception.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RequestUtil {

    private static final int LOWER_BOUND = 1000000;
    private static final int UPPER_BOUND = 10000000;
    private static final Random RANDOM = new Random();

    private RequestUtil() {
    }

    public static int getRandomRequestId() {
        return RANDOM.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }

    public static <T extends RestException> void validateEmptyFields(Class<T> exClass, PairFieldName... pairs) {
        for (PairFieldName pair : pairs) {
            validateEmptyField(exClass, pair.getFirst(), pair.getSecond());
        }
    }

    public static <T extends RestException, F> void validateEmptyField(Class<T> exClass, F field, String fieldName) {
        if (field == null ||
                field instanceof String && StringUtil.isOnlySpaces((String) field)) {
            constructAndThrowException(exClass, ErrorCode.REQUEST_VALIDATION_EMPTY_FIELDS,
                    StringUtil.isEmpty(fieldName) ? null : String.format("%s should be provided", fieldName));
        }
    }

    private static <T extends RestException> void constructAndThrowException(Class<T> exClass, ErrorCode code, String message) {

        if (exClass.equals(RepositoryException.class)) {
            throw new RepositoryException(code, message);
        } else if (exClass.equals(ServiceException.class)) {
            throw new ServiceException(code, message);
        } else if (exClass.equals(ControllerException.class)) {
            throw new ControllerException(code, message);
        } else {
            throw new RestException(code, message);
        }
    }

    public static Set<String> parametersSet(String... parameters) {
        Set<String> res = new HashSet<>();
        Collections.addAll(res, parameters);
        return res;
    }
}



