package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

/**
 * This class is the base level for other Actions as needed.
 */
public class BaseIdentityAction extends AbstractIdentityAction {

    @Override
    public void process(StringBuffer context, Identity identity, RootIdentityContentHandler root) throws SAXException {
        if (identity.getTemplateSegments().length == 1) {
            root.write(identity.getTemplateSegments()[0]);
        }
        else {
            root.write(identity.getTemplateSegments()[0]);
            root.write( context.toString() );
            root.write(identity.getTemplateSegments()[1]);
        }
    }
}
