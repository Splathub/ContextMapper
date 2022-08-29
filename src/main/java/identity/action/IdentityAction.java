package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;

public interface IdentityAction {

    void process(StringBuilder sb, Identity identity, GeneralContentHandler handler);

    void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler);
}
