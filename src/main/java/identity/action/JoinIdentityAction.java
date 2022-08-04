package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import org.xml.sax.SAXException;

/**
 * This class is a glorified StringBuffer class that may treat each appending differently through inheritance.
 */
public class JoinIdentityAction extends AbstractIdentityAction {

    public final String filler = "&nbsp;";
    protected boolean emptyTemplate;
    protected boolean initChecked;
    protected String[] segments;
    protected int segIndex = 0;

    @Override
    public void process(StringBuffer sb, Identity identity, RootIdentityContentHandler root) throws SAXException {

        if (emptyTemplate) {
            root.write(segments[0]);
        }
        else if (!initChecked) {
            initChecked = true;
            segments = identity.getTemplateSegments();

            if (segments.length == 1) {
                emptyTemplate = true;
            }
        }

        if (segIndex == segments.length - 1) {
            root.write(segments[segIndex++]);
            segIndex = 0;
        }

        root.write(segments[segIndex++]);
        root.write(sb.toString());
    }

    @Override
    public void endProcess(Identity identity, RootIdentityContentHandler root) throws SAXException {
        if (segments != null) {
            while (segIndex < segments.length - 1) {
                root.write(segments[segIndex++]);
                root.write(filler);
            }
            root.write(segments[segIndex]);
            root.write(identity.getTemplateSegments()[1]);
        }
    }

    protected void createSegment(String template) {
        segments = template.split("%s");
        segIndex = 0;
    }

}
