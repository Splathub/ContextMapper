package identityMaster.entity;

import identityMaster.HeaderFooterBuilder;
import identityMaster.KeyGenerator;

import java.util.HashMap;

public class IdentityMaster {

    private final String identityMasterPath;

    private KeyGenerator keyGenerator;
    private HeaderFooterBuilder hfBuilder;
    private HashMap<String, IdentityKeeper> sKHash;
    private HashMap<String, String> tKToSKHash;


    public IdentityMaster(String identityMasterPath) {
        this.identityMasterPath = identityMasterPath;
        //TODO: validate path or create

        // temp
        hfBuilder = new HeaderFooterBuilder();
        keyGenerator = new KeyGenerator();
        sKHash = new HashMap<>();
        tKToSKHash = new HashMap<>();
    }

    public IdentityKeeper getOrDefaultIdentityKeeper(String key) {
        return sKHash.getOrDefault(key, new IdentityKeeper(keyGenerator));
    }

    public void setIdentityKeeper(IdentityKeeper identityKeeper) {
        sKHash.merge(keyGenerator.generateStyleStrucKey(identityKeeper), identityKeeper, this::mergeIdentityKeepers);
    }

    public IdentityKeeper mergeIdentityKeepers(IdentityKeeper priority, IdentityKeeper secondary) {
        return priority;
    }



    public String getPath() {
        return identityMasterPath;
    }
}
