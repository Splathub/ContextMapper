package identity.passive;

import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

import java.util.Map;

public class PageBreakInsertPassiveAction extends AbstractPassiveAction {

    public PageBreakInsertPassiveAction(Map<String, Object> data) {
        super(data);
    }

    @Override
    public void process(StringBuffer context, RootIdentityContentHandler root) throws SAXException {

    }

}
