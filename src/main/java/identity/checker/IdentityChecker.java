package identity.checker;

import identity.entity.RootIdentityContentHandler;

public interface IdentityChecker {

    boolean check(StringBuffer sb, RootIdentityContentHandler root);

}
