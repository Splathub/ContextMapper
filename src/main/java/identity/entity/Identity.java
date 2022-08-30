package identity.entity;

import identity.action.IdentityAction;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Identity implements Cloneable {

    private IdentityAction action;
    private String template;
    private String[] templateSegments; // Only always two start and end tag (the wrap
    private boolean started = false;
    private Map<String, Object> args;

    public static final Set<String> REMOVE_CHECK = new HashSet(Arrays.asList("\t", "\n", " "));


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

    public Identity(IdentityAction action, String[] template, Map<String, Object> args) {
        this.action = action;
        if (args == null) {
            this.args = Collections.emptyMap();
        }
        else {
            this.args = args;
        }

        this.templateSegments = template; //TODO: cover before as selected  or one time and cycle, graph
        this.template = "";
    }

    public Identity(){}


    public void process(StringBuilder sb, GeneralContentHandler handler) {
        if (isValidString(sb)) {
            if (!started) {
                handler.write(templateSegments[0]);
                started = true;
            }
            action.process(sb, this, handler);
        }
    }

    public void finalProcess(StringBuilder sb, GeneralContentHandler handler) {
       if (isValidString(sb)) {
           if (!started) {
               handler.write(templateSegments[0]);
           }
           action.endProcess(sb, this, handler);
           handler.write(templateSegments[1]);
           started = false;
       }
       else if (started) {
           handler.write(templateSegments[1]);
           started = false;
       }
    }

    private boolean isValidString(StringBuilder sb) {
        if (sb.length() == 0) { //TODO: remove blank lines.. maybe
            return false;
        }
        return true;
    }

/*

    //TODO: overhead from casting from args;;;; FOWARDS/push
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

    */


    public IdentityAction getAction() {
        return action;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setAction(IdentityAction action) {
        this.action = action;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setTemplateSegments(String[] templateSegments) {
        this.templateSegments = templateSegments;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String[] getTemplateSegments() {
        return templateSegments;
    }


    public Object getArgs(String key) {
        return args.get(key);
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

    @Override
    public String toString() {
        return String.format("Identity (%s)", action.getClass().getName());
    }

}
