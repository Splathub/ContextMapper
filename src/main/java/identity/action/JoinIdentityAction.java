package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

/**
 * This class is a glorified StringBuffer class that may treat each appending differently through inheritance.
 */
public class JoinIdentityAction extends AbstractIdentityAction {

    protected StringBuffer sb;

    @Override
    public void process(StringBuffer context, Identity identity, RootIdentityContentHandler root) throws SAXException {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append(context);
    }

    @Override
    public void endProcess(Identity identity, RootIdentityContentHandler root) throws SAXException {
        if (sb != null) {
            root.write(sb.toString());
            sb = null;
        }
    }
}
