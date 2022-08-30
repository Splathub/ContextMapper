package identityMaster;

import identity.action.IdentityAction;
import identity.entity.Identity;
import identityMaster.entity.IdentityKeeper;
import identityMaster.entity.IdentityMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RootIdentityBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(RootIdentityBuilder.class);
    private final String identityModelPath = "identity/models/";

    private final IdentityMaster master;
    private HashMap<String, Identity> identityMap;

    public RootIdentityBuilder(IdentityMaster master) {
        this.master = master;
    }

    // Object array of File Cat, and Map<>; return name instead, maybe
    public File syncedCATFileWithRootIdentityMap() {
        Map<Integer, Integer> catCounts = new HashMap<>();
        int sum = 0;

        identityMap = new HashMap<>();
        File trainFile = new File(identityModelPath+master.getName()+".train");

        FileWriter myWriter = null;
        try {
            int id = 0;
            int count;

            StringBuilder sb;
            Class clazz;
            Identity identity;
            IdentityAction action;
            myWriter = new FileWriter(trainFile);
            
            for(IdentityKeeper keeper : master.getsKHash().values()) {

                if (keeper.getAllowedProxies() != null && keeper.getAllowedProxies().size() > 1) {
                    LOG.info("Keeper with more than one allowed proxies");
                }

                if (keeper.getIdentityName() == null) {
                    LOG.warn("Null class Action, not included: " + keeper.getTag());
                }
                else if (!keeper.getTag().equalsIgnoreCase("table")) {

                    clazz = Class.forName(keeper.getIdentityName());
                    action = (IdentityAction) clazz.newInstance();

                    count=0;
                    for (List<String> chunk : keeper.getTextMarks()) {

                        sb = new StringBuilder();
                        for (String text : chunk) {
                            sb.append(text.toLowerCase().replaceAll("^#%(.*?):|\\.|,|\"|\\(|\\)|:", "").trim());
                        }

                        if (sb.length() != 0) {
                            myWriter.write(id + " ");
                            myWriter.write(sb.toString());
                            myWriter.write("\n");
                            count++;
                        }
                    }

                    identity = new Identity(action, TemplateUtil.mkTemplate(keeper), keeper.getArgs());
                    identityMap.put(String.valueOf(id), identity);

                    sum += count;
                    catCounts.put(id, count);
                    id++;
                }
                else {
                    LOG.warn("Keeper invalid or dismissed: " + keeper.getTag());
                }
            }
            LOG.info("Successfully wrote synced CAT and SimpleRootMap ids: " + id);
        } catch (IOException e) {
            LOG.error("Unknown IO error");
        } catch (ClassNotFoundException e) {
            LOG.error("File not found");
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error("Unknown error");
        }
        finally {
            if (myWriter != null){
                try {
                    myWriter.close();
                }
                catch (Exception e) {
                    LOG.error("Failed to closed writer for CAT file: " + trainFile.getAbsolutePath());
                }
            }
        }

        int average = sum / catCounts.size();
        LOG.info("Average CAT data points: " + average);

        return trainFile;
    }

    public Map<String, String> buildHashedIdentityMap() {
       return null; // TODO: hash keepers
    }

    public HashMap<String, Identity> buildRootIdentityMap() {
        HashMap<String, Identity> map = new HashMap<>();

        int id = 0;
        Class clazz = null;
        Identity identity;
        IdentityAction action;
        try {
            for (IdentityKeeper keeper : master.getsKHash().values()) {
                if (!keeper.getTag().equalsIgnoreCase("table")) {   //TODO: tables and similar need special text/styleStruc containment

                    if (keeper.getIdentityName() == null) {
                        LOG.warn("Null class Action :" + keeper.getTag());
                    } else {

                        clazz = Class.forName(keeper.getIdentityName());
                        action = (IdentityAction) clazz.newInstance();

                        identity = new Identity(action, TemplateUtil.mkTemplate(keeper), keeper.getArgs());

                        map.put(String.valueOf(id), identity);
                    }
                }
                id++;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("IdentityMaster, Invalid class action" + clazz.getName());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return map;
    }

    public File writeCatFile() {
        File trainFile = new File(identityModelPath+master.getName()+".train");

        try {
            FileWriter myWriter = new FileWriter(trainFile);
            int i = 0;
            StringBuilder sb;
            for(Map.Entry<String, IdentityKeeper> entry : master.getsKHash().entrySet()) {
                String cat = entry.getKey();

                for (List<String> chunk : entry.getValue().getTextMarks()) {

                    if (!entry.getValue().getTag().equalsIgnoreCase("table")) {

                        sb = new StringBuilder();
                        for (String text : chunk) {
                            sb.append(text.toLowerCase().replaceAll("^#%(.*?):|\\.|,|\"|\\(|\\)|:", "").trim());
                        }

                        if (sb.length() != 0) {
                            myWriter.write(++i + " ");
                            myWriter.write(sb.toString());
                            myWriter.write("\n");
                        }
                    }
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file ids: " + i);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return trainFile;
    }

    public Map<String, Identity> getIdentityMap() {
        return identityMap;
    }

}
