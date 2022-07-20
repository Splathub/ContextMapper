package identity.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Part {
    private final Logger LOG = LoggerFactory.getLogger(Part.class);

    private Identity[] identities;
    private final int window = 3;
    private int onPoint=0;

    public Part(Identity[] identities) {
        this.identities = identities;
    }

    private void findIdentity(String str) {
        for(int i=onPoint; i < identities.length && i < identities.length + window; i++) {
            if (identities[onPoint].check(str)) {
                if (onPoint != i) {
                    //TODO: last chance process of Identity?
                }
                onPoint = i;
            }
        }
    }

    public String process(String str, RootIdentityContentHandler root) {
        findIdentity(str);
        return identities[onPoint].process(str, root);
    }

}
