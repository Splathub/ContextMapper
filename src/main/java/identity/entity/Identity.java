package identity.entity;

import identity.action.IdentityAction;
import org.xml.sax.SAXException;

import java.util.Collections;
import java.util.Map;

public class Identity {

    private final IdentityAction action;
    private final String template;
    private final String[] templateSegments;
    private int segIndex = 0;
    private final Map<String, Object> args;


    public Identity(IdentityAction action, String template, Map<String, Object> args) {
        this.action = action;
        if (args == null) {
            this.args = Collections.emptyMap();
        }
        else {
            this.args = args;
        }

        this.template = template; //TODO: cover before as selected  or one time and cycle, graph
        templateSegments = template.split("%s");
    }

    //TODO: overhead from casting from args
    public void process(StringBuffer sb, Part part, RootIdentityContentHandler root) throws SAXException {
        Object value;
        // Pre-processes for Part effects

        value = args.get("push");
        if (value != null) {    // removed not 0 ck
            part.pushPoint((int)value);
        }

        value = args.get("range");
        if (value != null) {    // removed not defaultRange ck
            part.adjustedRange((int)value);
        }

        value = args.get("trim");
        if (!((String)value).isEmpty()) {
            int index = sb.indexOf((String)value);
            if (index >= 0) {
                sb.delete(index, ((String)value).length());
            }
        }

        value = args.get("split");
        if (value != null) {
            //TODO: improve split feature preformance
            // String delimiter = "((?<="+ args.get("split") +"))";
            int idx = sb.indexOf((String) args.get("split"));
            if (idx >= 0) {
                action.process(sb.substring(0, idx+1), this, root);
                action.process(sb.substring(idx+1), this, root);
            }
            else {
                action.process(sb, this, root);
            }

            //for (String str: sb.toString().split(delimiter)) {
            //      action.process(str, this, root);
            //  }
        } else {
            action.process(sb, this, root);
        }

        // Post-Process
        value = args.get("include");
        if (value != null) {    // Removed not 0 ck
            part.includedProcess((int)value, sb, root);
        }
    }

    public void finalProcess(RootIdentityContentHandler root) throws SAXException {
        action.endProcess(this, root);
        if (args.containsKey("endWith")) {
            root.write((String) args.get("endWith"));
        }
    }

    public IdentityAction getAction() {
        return action;
    }

    public String getTemplate() {
        return template;
    }

    public String[] getTemplateSegments() {
        return templateSegments;
    }


    public Object getData(String key) {
        return args.get(key);
    }

    @Override
    public String toString() {
        return String.format("Identity (%s)", action.getClass().getName());
    }

}
