package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;

public class AnchorIdentityAction implements IdentityAction {


    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {

    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {

    }

    //TODO: corrects hyperlinks to correct domain
}
