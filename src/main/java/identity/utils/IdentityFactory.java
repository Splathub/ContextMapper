package identity.utils;

import identity.action.IdentityAction;
import identity.entity.Identity;
import identity.exception.IdentityCrisisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This singleton class is responsible for create all Identity objects and their IdentityChecker and IdentityAction objects.
 * IdentityChecker and IdentityAction are both initialize through a given class String name that should match a current
 * class. Each of these Classes should hold a constructor that takes a Map class only. The setup for most of these
 * classes will be defined through a Map<String, Object>.
 */
public class IdentityFactory {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityFactory.class);

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

    public static Identity createIdentity(Map<String, Object> data) {
        Class clazz = null;
        String msgErr;

        if (data == null) {
            data = new HashMap<>();
        }

        try {
            clazz = Class.forName("identity.checker." + data.getOrDefault("checker", "ToggleChecker"));
            Identity checker = (Identity) clazz.getDeclaredConstructor(Map.class).newInstance(data.get("args"));

            clazz = Class.forName("identity.action." + data.getOrDefault("action", "BaseIdentityAction"));
            IdentityAction action = (IdentityAction) clazz.getDeclaredConstructor().newInstance();

            return new Identity(
                    action,
                    (String) data.getOrDefault("template", ""),
                    (Map<String, Object>) data.get("args"));

            //TODO: old idenity needs to move features in args

/*                    (int) data.getOrDefault("push", 0),
                    (int) data.getOrDefault("include", 0),
                    (int) data.getOrDefault("range", Part.getDefaultWindow()),
                    (String) data.getOrDefault("trim", "")*/

        } catch (ClassNotFoundException e) {
            if (clazz == null) {
                msgErr = "Missing 'checker' or 'action' for Identity";
            } else {
                msgErr = String.format("Given class not found: '%s'", clazz.getName());
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            msgErr = String.format("Class not setup correctly, needs a Map<String, Object> constructor: '%s'", clazz.getName());
        } catch (InvocationTargetException e) {
            msgErr = String.format("Class parameters not met: '%s'", e.getCause().getMessage());
        }

        LOG.error(String.format("%s, Data: %s", msgErr, data));
        throw new IdentityCrisisException(msgErr);
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
