package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

public interface IdentityAction {

    void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException;

    void process(StringBuffer context, Identity identity, RootIdentityContentHandler root) throws SAXException;

    void process(String str, Identity identity, RootIdentityContentHandler root) throws SAXException;

    void endProcess(Identity identity, RootIdentityContentHandler root) throws SAXException;

    void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) throws SAXException;
}
