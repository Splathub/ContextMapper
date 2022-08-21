package identityMaster.entity;

import identity.entity.Identity;
import identityMaster.KeyGenerator;

import java.io.Serializable;
import java.util.Map;

public class RootIdentity implements Serializable {

    private KeyGenerator keyGenerator;
    private Map<String, String> contentMapping;
    private Map<String, Identity> identityMapping;

}
