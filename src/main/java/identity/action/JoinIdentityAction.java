package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;

/**
 * This class is a glorified StringBuffer class that may treat each appending differently through inheritance.
 */
public class JoinIdentityAction implements IdentityAction {

    public final String filler = "&nbsp;";
    protected boolean emptyTemplate;
    protected boolean initChecked;
    protected String[] segments;
    protected int segIndex = 0;

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        //TODO: implement JOIN / table action

        if (emptyTemplate) {
            handler.write(segments[0]);
        }
        else if (!initChecked) {
            initChecked = true;
            segments = identity.getTemplateSegments();

            if (segments.length == 1) {
                emptyTemplate = true;
            }
        }

        if (segIndex == segments.length - 1) {
            handler.write(segments[segIndex++]);
            segIndex = 0;
        }

        handler.write(segments[segIndex++]);
        handler.write(sb.toString());
    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        if (segments != null) {
            while (segIndex < segments.length - 1) {
                handler.write(segments[segIndex++]);
                handler.write(filler);
            }
            handler.write(segments[segIndex]);
            handler.write(identity.getTemplateSegments()[1]);
        }
    }

}
