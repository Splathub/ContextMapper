package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

public class IgnoreIdentityAction extends AbstractIdentityAction {

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException {

    }

    @Override
    public void process(StringBuffer context, Identity identity, RootIdentityContentHandler root) throws SAXException {

    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException {

    }
}
