package Identity.utils;

import Identity.Identity;
import Identity.action.IdentityAction;
import Identity.checker.IdentityChecker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This singleton class is responsible for create all Identity objects and their IdentityChecker and IdentityAction objects.
 * IdentityChecker and IdentityAction are both initialize through a given class String name that should match a current
 * class. Each of these Classes should hold a constructor that takes a Map.class only. The setup for most of these
 * classes will be defined through a Map<String, Object>.
 */
public class IdentityFactory {

    private static IdentityFactory factory;
    private  Set<Class> checkers;
    private  Set<Class> actions;

    private IdentityFactory() {
       // checkers = findAllClassesUsingClassLoader("src/main/java/Identity/checker");
        //actions = findAllClassesUsingClassLoader("src/main/java/Identity/action");
    }

    public static IdentityFactory getInstance() {
        if (factory == null) {
            factory = new IdentityFactory();
        }
        return factory;
    }

    public static Identity createIdentity(Map<String, Object> data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Class clazz = Class.forName("Identity.checker." + data.get("checker"));
        IdentityChecker checker = (IdentityChecker) clazz.getDeclaredConstructor(Map.class).newInstance(data);

        clazz = Class.forName("Identity.action." + data.get("action"));
        IdentityAction action = (IdentityAction) clazz.getDeclaredConstructor(Map.class).newInstance();

        return new Identity(
                checker,
                action,
                (String) data.get("name"),
                (Map<String, Object>) data.get("args")
        );
    }

    public Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

}
