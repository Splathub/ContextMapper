package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;

/**
 * This class is the base level for other Actions as needed.
 */
public class BaseIdentityAction implements IdentityAction {

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        handler.write(sb.toString());
    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        handler.write(sb.toString());
    }

}
