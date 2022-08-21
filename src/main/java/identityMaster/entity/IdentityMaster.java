package identityMaster.entity;

import identityMaster.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IdentityMaster implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityKeeper.class);

    private final String identityMasterPath = "identity/rootMaps";
    private String name;

    private KeyGenerator keyGenerator;
    private HashMap<String, IdentityKeeper> sKHash;
    private HashMap<String, String> tKToSKHash;


    public IdentityMaster(String name) throws IOException {
        this.name = name;
        //TODO: validate path or create

        // temp
        keyGenerator = new KeyGenerator();
        sKHash = new HashMap<>();
        tKToSKHash = new HashMap<>();
    }

    public void generateAllTextKeys() {
        for(IdentityKeeper keeper : sKHash.values()) {
            keyGenerator.generateContextKey(keeper);
        }
    }

    public IdentityKeeper getOrDefaultIdentityKeeper(String key) {
        return sKHash.getOrDefault(key, new IdentityKeeper());
    }


    public void setIdentityKeeper(IdentityKeeper identityKeeper) {
        sKHash.merge(keyGenerator.generateStyleStrucKey(identityKeeper), identityKeeper, this::mergeIdentityKeepers);
    }

    public IdentityKeeper mergeIdentityKeepers(IdentityKeeper priority, IdentityKeeper secondary) {
        return priority;
    }

    public void mergeIdentityKeeper(IdentityKeeper keeper) {
        //TODO:
    }

    public void mergeElement(Element element) {
        mergeElement(element, null, null);
    }

    public void mergeElement(Element element, String ownProxy) {
        mergeElement(element, ownProxy, null);
    }

    public void mergeElement(Element element, String ownProxy, String allowedProxy) {
        List<String> remove = new LinkedList<>();
        remove.add("");
        remove.add(" ");

        element.getTextSlugs().removeAll(remove);

        if (element.getTextSlugs().isEmpty() || element.getText().trim().isEmpty()) {
            LOG.warn("Empty text Element, ignored");
            return;
        }


        String ssKey = keyGenerator.generateStyleStrucKey(element);
        IdentityKeeper keeper = sKHash.get(ssKey);
        if (keeper == null) {
            keeper = new IdentityKeeper(element, ownProxy, allowedProxy);
        }
        else {
            keeper.addElement(element);
        }
        sKHash.put(ssKey, keeper);

        /*
        sKHash.compute(ssKey, (key, value) ->
                (value == null)?
                        new IdentityKeeper(this, element)
                        : value.addElement(element));*/
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return identityMasterPath + name;
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
