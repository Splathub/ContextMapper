package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

/**
 * This class is the base level for other Actions as needed.
 */
public class BaseIdentityAction extends AbstractIdentityAction {

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException {
        handler.write(identity.getTemplateSegments()[0]);
        handler.write(sb.toString());
    }

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

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException {
        handler.write(sb.toString());
        handler.write(identity.getTemplateSegments()[1]);
    }


}
