package identity.entity;

import identityMaster.KeyGenerator;

import java.io.Serializable;
import java.util.Map;

public class HashedTextToAction implements TextToAction, Serializable {

    private KeyGenerator keyGenerator;
    private Map<String, String> contentMap; // txKey to ID
    private Map<String, Identity> identityMap;  // ID to Identity


    @Override
    public Identity identify(StringBuilder sb) {
        String key = keyGenerator.generateContextKey(sb);
        String ssKey = contentMap.get(key);
        return identityMap.get(ssKey);
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public Map<String, String> getContentMap() {
        return contentMap;
    }

    public void setContentMap(Map<String, String> contentMap) {
        this.contentMap = contentMap;
    }

    public Map<String, Identity> getIdentityMap() {
        return identityMap;
    }

    public void setIdentityMap(Map<String, Identity> identityMap) {
        this.identityMap = identityMap;
    }
}
