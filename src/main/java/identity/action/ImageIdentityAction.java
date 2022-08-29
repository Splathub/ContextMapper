package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;

public class ImageIdentityAction implements IdentityAction {

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {

    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {

    }

    //TODO: selects file and saves, handle image tags
}
