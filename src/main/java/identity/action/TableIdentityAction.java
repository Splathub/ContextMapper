package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import identity.exception.ParameterException;
import org.xml.sax.SAXException;

public class TableIdentityAction extends JoinIdentityAction {

    private boolean initChecked;
    private boolean emptyTemplate;
    private final String filler = "&nbsp;";
    private boolean inHead;
    private String row;

    private String[] segments;
    private int segIndex = 0;

    @Override
    public void process(StringBuffer sb, Identity identity, RootIdentityContentHandler root) throws SAXException {

        if (emptyTemplate) {
            root.write(sb.toString());
        }
        else if (!initChecked) {
            initChecked = true;

            if (identity.getTemplateSegments().length != 2) {
                throw new ParameterException("TableIdentityAction 'template' only accepts 1 value wraps of only table tags");
            }

            root.write(identity.getTemplateSegments()[0]);

            //TODO: special parse for template types to each insert no format for speed
            String head = (String) identity.getData("head");
            if (head != null) {
                createSegment(head);
                // template doesn't have any values to insert, All values are expected to be wrapped
                if (segments.length == 1) {
                    root.write(segments[0]);
                }
                else {
                    inHead = true;
                }
            }

            row = (String) identity.getData("row");
            if (row == null) {
                emptyTemplate = true;
            }

            if (segments == null || !inHead) {
                createSegment(row);
            }
        }
        else {

            if (segIndex == segments.length - 1) {
                root.write(segments[segIndex++]);
                segIndex = 0;

                if (inHead) {
                    createSegment(row);
                    inHead = false;
                }
            }
            else {
                root.write(segments[segIndex++]);
                root.write(sb.toString());
            }

        }
    }

    @Override
    public void endProcess(Identity identity, RootIdentityContentHandler root) throws SAXException {
        while(segIndex < segments.length-1) {
            root.write(segments[segIndex++]);
            root.write(filler);
        }
        root.write(segments[segIndex]);
        root.write(identity.getTemplateSegments()[1]);
    }

    private void createSegment(String template) {
        segments = template.split("%s");
        segIndex = 0;
    }

}
