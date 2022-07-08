package Identity.checker;

import Identity.annotation.Checker;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * Utility class to build Identity checkers
 */
public class CheckerFactory {
  private static final Map<String, Class<IdentityChecker>> checkers = new HashMap<>();

  static {
    resolveCheckers();
  }

  /**
   * initializes checker factory class; This method must
   * be called on object initialization to scan the root package and
   * retrieve all classes annotated with the Checker key word;
   */
  private static void resolveCheckers() {
    // retrieve all class annotated with checker within Identity package
    Set<Class<?>> annotated = new Reflections("Identity")
            .get(SubTypes.of(TypesAnnotated.with(Checker.class)).asClass());
    for (Class<?> clazz : annotated) {
      Checker checkerAnnotation = clazz.getAnnotation(Checker.class);
      checkers.put(checkerAnnotation.name(), (Class<IdentityChecker>) clazz);
    }
  }

  /**
   * Generates a {@link IdentityChecker} depending on the given config;
   *
   * @param config Hashmap with a type which is the type of identity check to perform;
   * @return {@link IdentityChecker}
   * @throws RuntimeException if type is not recognized or if the information
   *                          provided in the hashmap are of the wrong types
   */
  public static IdentityChecker build(Map<String, Object> config) {
    try {
      Class<IdentityChecker> correspondingClass = checkers.get(config.get("type"));
      if (correspondingClass == null)
        throw new RuntimeException("Invalid type passed to Identity builder : " +
                                           config.get("type"));
      Constructor<IdentityChecker> constructor = correspondingClass
              .getDeclaredConstructor(Map.class);
      return constructor.newInstance(config);
    } catch (Exception e) {
      // TODO : Change this runtime exception to a custom error;
      throw new RuntimeException(e);
    }
  }
}
