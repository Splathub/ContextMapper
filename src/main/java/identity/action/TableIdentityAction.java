package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import identity.exception.ParameterException;

public class TableIdentityAction extends JoinIdentityAction {

    private boolean hasFooter;
    private boolean inHead;
    private String row;

    //TODO: correct segments for get from args of Identity for head, foot and row, col ..
    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {

        if (emptyTemplate) {
            handler.write(sb.toString());
            return;
        }

        if (!initChecked) {
            initChecked = true;

            if (identity.getTemplateSegments().length != 2) {
                throw new ParameterException("TableIdentityAction 'template' only accepts 1 value wraps of only table tags");
            }

            handler.write(identity.getTemplateSegments()[0]);

            //TODO: special parse for template types to each insert no format for speed
            String head = (String) identity.getArgs("head");
            if (head != null) {
                //createSegment(head);
                // template doesn't have any values to insert, All values are expected to be wrapped
                if (segments.length == 1) {
                    handler.write(segments[0]);
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
                //createSegment(row);
            }
        }

         if (segIndex == segments.length - 1) {
             handler.write(segments[segIndex++]);
            segIndex = 0;

            if (inHead) {
               // createSegment(row);
                inHead = false;
            }
        }

        handler.write(segments[segIndex++]);
        handler.write(sb.toString());
    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        //TODO: implement footer and postpone insertion to identify a footer
        while(segIndex < segments.length-1) {
            handler.write(segments[segIndex++]);
            handler.write(filler);
        }
        handler.write(segments[segIndex]);
        handler.write(identity.getTemplateSegments()[1]);
    }

}
