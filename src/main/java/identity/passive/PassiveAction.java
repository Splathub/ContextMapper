package identity.passive;

import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

public interface PassiveAction {

    void process(StringBuffer context, RootIdentityContentHandler root) throws SAXException;

}
