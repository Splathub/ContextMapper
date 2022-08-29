package identity.action;

import identity.entity.Identity;
import identity.entity.RootIdentityContentHandler;
import identity.exception.ParameterException;
import org.xml.sax.SAXException;

public class TableIdentityAction extends JoinIdentityAction {

    private boolean hasFooter;
    private boolean inHead;
    private String row;


    @Override
    public void process(StringBuffer sb, Identity identity, RootIdentityContentHandler root) throws SAXException {

        if (emptyTemplate) {
            root.write(sb.toString());
            return;
        }

        if (!initChecked) {
            initChecked = true;

            if (identity.getTemplateSegments().length != 2) {
                throw new ParameterException("TableIdentityAction 'template' only accepts 1 value wraps of only table tags");
            }

            root.write(identity.getTemplateSegments()[0]);

            //TODO: special parse for template types to each insert no format for speed
            String head = (String) identity.getArgs("head");
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

            row = (String) identity.getArgs("row");
            if (row == null) {
                emptyTemplate = true;
            }

            if (segments == null || !inHead) {
                createSegment(row);
            }
        }

         if (segIndex == segments.length - 1) {
            root.write(segments[segIndex++]);
            segIndex = 0;

            if (inHead) {
                createSegment(row);
                inHead = false;
            }
        }

        root.write(segments[segIndex++]);
        root.write(sb.toString());

    }

    @Override
    public void endProcess(Identity identity, RootIdentityContentHandler root) throws SAXException {
        //TODO: implement footer and postpone insertion to identify a footer
        while(segIndex < segments.length-1) {
            root.write(segments[segIndex++]);
            root.write(filler);
        }
        root.write(segments[segIndex]);
        root.write(identity.getTemplateSegments()[1]);
    }


}
