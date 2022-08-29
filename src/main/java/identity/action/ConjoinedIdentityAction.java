package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import org.xml.sax.SAXException;

public class ConjoinedIdentityAction extends AbstractIdentityAction {

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException {

    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException {

    }

    //TODO: process logic for tesxt with inline font/b by text location or more complex
}
