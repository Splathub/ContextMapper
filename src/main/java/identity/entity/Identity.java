package identity.entity;

import identity.action.IdentityAction;
import identity.checker.IdentityChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.util.Map;

/**
 * This class is responsible for identifying context as a String or char[] by the parameters of the IdentityChecker. If
 * found TRUE the IdentityAction will preform it's changes if any. When identified, the attribute would be used to
 * assign the context by the ContentHandler.
 */
public class Identity {
    private final Logger LOG = LoggerFactory.getLogger(Identity.class);

    private final IdentityAction action;
    private final IdentityChecker checker;
    private final String template;
    private final String[] templateSegments;
    private int segIndex = 0;
    private final Map<String, Object> args;

    private int push;
    private int include;
    private int range;
    private String trim;


    public Identity(IdentityChecker checker, IdentityAction action, String template, Map<String, Object> args, int push, int include, int range, String trim) {
        this.checker = checker;
        this.action = action;
        this.args = args;
        this.template = template; //TODO: cover before as selected  or one time and cycle, graph
        templateSegments = template.split("%s");

        this.push = push;
        this.include = include;
        this.range = range;
        this.trim = trim;
    }

    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        return checker.check(sb, root);
    }

    public void process(StringBuffer sb, Part part, RootIdentityContentHandler root) throws SAXException {
        // Pre-processes for Part effects
        if (push != 0) {
            part.pushPoint(push);
        }

        if (range != part.getDefaultWindow()) {
            part.adjustedRange(range);
        }

        if(!trim.isEmpty()) {
            int index = sb.indexOf(trim);
            if (index > 0) {
                sb.delete(index, trim.length());
            }
        }

        action.process(sb, this, root );

        if (include != 0) {
            part.includedProcess(include, sb, root);
        }
    }

    public void finalProcess(RootIdentityContentHandler root) throws SAXException {
        action.endProcess(this, root);
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
        return String.format("Identity %s (%s %s)", checker.getClass().getName(), action.getClass().getName());
    }

}
