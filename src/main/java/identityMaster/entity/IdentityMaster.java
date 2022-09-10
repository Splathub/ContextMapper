package identityMaster.entity;

import constants.Constants;
import identityMaster.KeyGenerator;
import identityMaster.ModeledTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class IdentityMaster implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityKeeper.class);

    private String name;
    private KeyGenerator keyGenerator;
    private final ModeledTransformer modeledTransformer;
    private HashMap<String, IdentityKeeper> sKHash;
    private HashMap<String, String> tKToSKHash;
    private HashMap<Integer, EmbeddedTextStyle> embeddedTextStylePool;
    private int nextEmbeddedTextStyleID = 0;


    public IdentityMaster(String name) throws IOException {
        this();
        this.name = name;
        //TODO: validate path or create

        // temp
        keyGenerator = new KeyGenerator();
        sKHash = new HashMap<>();
        tKToSKHash = new HashMap<>();
        embeddedTextStylePool = new HashMap<>();
    }

    public IdentityMaster() {
        modeledTransformer = ModeledTransformer.getInstance();
    }

    public void generateAllTextKeys() {
        for(IdentityKeeper keeper : sKHash.values()) {
            keyGenerator.generateContextKey(keeper);
        }
    }

   // public IdentityKeeper getOrDefaultIdentityKeeper(String key) {
   //     return sKHash.getOrDefault(key, new IdentityKeeper());
   // }

    public void mergeElement(Element element) {
        element.getTextSlugs().removeAll(Constants.REMOVE);

        /*
        if (element.getTextSlugs().isEmpty() || element.getText().trim().isEmpty()) {
            //LOG.warn("Empty text Element, ignored"); TODO: handle?
            return;
        }

         */

        String ssKey = keyGenerator.generateStyleStrucKey(element);
        if (ssKey != null) {
            IdentityKeeper keeper = sKHash.get(ssKey);
            if (keeper == null) {
                keeper = new IdentityKeeper(element);
                sKHash.put(ssKey, keeper);
            } else {
                keeper.addElement(element);
            }

            String parentSSKey = null;
            try {
                parentSSKey = keeper.getRootParentSSKey();
                if (parentSSKey != null) {
                    LOG.debug("Incremented rootParent child count" + parentSSKey);
                    sKHash.get(parentSSKey).plusChild();
                    parentSSKey = keeper.getParentSSKey();
                    if (!parentSSKey.equalsIgnoreCase(keeper.getRootParentSSKey())) {
                        LOG.debug("Incremented parent child count" + parentSSKey);
                        sKHash.get(parentSSKey).plusChild();
                    }
                }
            }
            catch (NullPointerException e) {
                LOG.error("Parent not found! Keeper parent: "+parentSSKey);
            }


        /*
        sKHash.compute(ssKey, (key, value) ->
                (value == null)?
                        new IdentityKeeper(this, element)
                        : value.addElement(element));*/
        }
        else {
            LOG.warn("Null ssKey for builder");
        }
    }

    public int getEmbeddedTextStyleID(String text, List<String[]> insertsInOrder){
        EmbeddedTextStyle embeddedTextStyle = new EmbeddedTextStyle(modeledTransformer);
        embeddedTextStyle.buildSelector(text, insertsInOrder);
        embeddedTextStylePool.put(nextEmbeddedTextStyleID, embeddedTextStyle);
        return nextEmbeddedTextStyleID++;
    }

    public String getSSKey(Element element) {
        return keyGenerator.generateStyleStrucKey(element);
    }

    public HashMap<Integer, EmbeddedTextStyle> getEmbeddedTextStylePool() {
        return embeddedTextStylePool;
    }

    public void setEmbeddedTextStylePool(HashMap<Integer, EmbeddedTextStyle> embeddedTextStylePool) {
        this.embeddedTextStylePool = embeddedTextStylePool;
    }

    public int getNextEmbeddedTextStyleID() {
        return nextEmbeddedTextStyleID;
    }

    public void setNextEmbeddedTextStyleID(int nextEmbeddedTextStyleID) {
        this.nextEmbeddedTextStyleID = nextEmbeddedTextStyleID;
    }

    public String getPath() {
        return Constants.IDENTITY_MAPS_PATH + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public HashMap<String, IdentityKeeper> getsKHash() {
        return sKHash;
    }

    public void setsKHash(HashMap<String, IdentityKeeper> sKHash) {
        this.sKHash = sKHash;
    }

    public HashMap<String, String> gettKToSKHash() {
        return tKToSKHash;
    }

    public void settKToSKHash(HashMap<String, String> tKToSKHash) {
        this.tKToSKHash = tKToSKHash;
    }
}
