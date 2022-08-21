package identityMaster;

import identity.action.IdentityAction;
import identity.entity.Identity;
import identityMaster.entity.IdentityKeeper;
import identityMaster.entity.IdentityMaster;
import identityMaster.entity.RootIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootIdentityBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(RootIdentityBuilder.class);

    private final String identityModelPath = "identity/models/";
    private final IdentityMaster master;

    private RootIdentity root;

    public RootIdentityBuilder(IdentityMaster master) {
        this.master = master;
    }

    public Map<String, Identity> buildRootIdentityMap() {
        Map<String, Identity> map = new HashMap<>();

        int i = 0;
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

                        map.put(String.valueOf(i), identity);
                    }
                }
                i++;
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

    public File createRootIdentityModel() {
        ModeledTransformer mt = new ModeledTransformer();
        return mt.train(writeCatFile());
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

}
