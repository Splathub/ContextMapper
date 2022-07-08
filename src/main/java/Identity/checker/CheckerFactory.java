package Identity.checker;
import java.util.Map;

/**
 * Utility class to build Identity checkers
 */
public class CheckerFactory {
    /**
     * Generates a {@link IdentityChecker} depending on the given config;
     *
     * @param config Hashmap with a type which is the type of identity check to perform;
     * @return {@link IdentityChecker}
     *
     * @throws RuntimeException if type is not recognized or if the information
     * provided in the hashmap are of the wrong types
     */
    public static IdentityChecker build(Map<String, Object> config) {

        // Get Identity Checker type identifier
        String type = (String) config.get("type");

        if (type.equals("MATCHING_TEXT"))
            return new MatchingTextChecker((String) config.get("data"));

        if (type.equals("HASH_KEYWORD"))
            return new HashKeywordChecker((String) config.get("data"));

        // TODO : Change this runtime exception to a custom error;
        throw  new RuntimeException("Invalid type passed to Identity builder : " + type);
    }
}
