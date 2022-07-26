package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

public class IgnoreIdentityAction extends AbstractIdentityAction {

    @Override
    public void process(StringBuffer context, Identity identity, RootIdentityContentHandler root) throws SAXException {

    }
}
