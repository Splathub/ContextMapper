package identity.utils;

import identity.entity.Identity;
import identity.action.IdentityAction;
import identity.checker.IdentityChecker;
import identity.entity.Part;
import identity.entity.RootIdentityContentHandler;
import identity.exception.IdentityCrisisException;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
            IdentityChecker checker = (IdentityChecker) clazz.getDeclaredConstructor(Map.class).newInstance(data.get("args"));

            clazz = Class.forName("identity.action." + data.getOrDefault("action", "BaseIdentityAction"));
            IdentityAction action = (IdentityAction) clazz.getDeclaredConstructor().newInstance();

            return new Identity(
                    checker,
                    action,
                    (String) data.getOrDefault("template", ""),
                    (Map<String, Object>) data.get("args"),
                    (int) data.getOrDefault("push", 0),
                    (int) data.getOrDefault("include", 0),
                    (int) data.getOrDefault("range", Part.getDefaultWindow()),
                    (String) data.getOrDefault("trim", "")
            );
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

    public static RootIdentityContentHandler createRootIdentity(Map<String, Object> data, String partsPath) {
        Class clazz = null;
        String msgErr;

        if (partsPath == null) {
            throw new IdentityCrisisException("Invalid part path");
        }

        if (data == null) {
            data = new HashMap<>();
        }

        try {
            String name = (String) data.get("name");
            if (name == null) {
                throw new IdentityCrisisException("Missing 'name'");
            }

         //   clazz = Class.forName("identity.action." + data.getOrDefault("defaultAction", "BaseIdentityAction.class"));
          //  IdentityAction defaultAction = (IdentityAction) clazz.getDeclaredConstructor(Map.class).newInstance(data.getOrDefault("args", null)); //no args

            ArrayList<Map<String, String>> map = (ArrayList<Map<String, String>>) data.get("parts");
            Pair<IdentityChecker, String>[] parts;
            if (map == null) {
                parts = new Pair[0];
            }
            else {
                parts = new Pair[map.size()];
                int i = 0;
                for (Map<String, String> partInfo : map) {
                    String part = partInfo.getOrDefault("part", null);
                    if (part != null) {
                        clazz = Class.forName("identity.checker." + partInfo.getOrDefault("checker", "HasKeywordChecker"));
                        IdentityChecker checker = (IdentityChecker) clazz.getDeclaredConstructor(Map.class).newInstance(partInfo);

                        parts[i++] = new Pair<>(checker, part);
                    } else {
                        throw new IdentityCrisisException("Missing 'part'");
                    }
                }
            }

            return new RootIdentityContentHandler(
                    name,
                    partsPath,
                   // defaultAction,
                    parts
            );
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
