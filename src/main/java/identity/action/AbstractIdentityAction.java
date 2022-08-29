package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

public abstract class AbstractIdentityAction implements IdentityAction {

    private int segIndex=0;

    @Override
    public void process(StringBuffer context, Identity identity, RootIdentityContentHandler root) throws SAXException {
        root.write( context.insert(0, '>')
                .insert(0, root.getTag())
                .insert(0, '<')
                .append("</")
                .append(root.getTag())
                .append('>')
                .toString());
    }

    @Override
    public void process(String str, Identity identity, RootIdentityContentHandler root) throws SAXException {
        process(new StringBuffer(str), identity, root);
    }

    @Override
    public void endProcess(Identity identity, RootIdentityContentHandler root) throws SAXException {

    }
}
