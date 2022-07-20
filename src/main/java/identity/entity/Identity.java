package identity.entity;

import identity.action.IdentityAction;
import identity.action.IdentityActionType;
import identity.checker.IdentityChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    //private final String template;
    private final Map<String, Object> args;


    public Identity(IdentityChecker checker, IdentityAction action, Map<String, Object> args) {
        this.checker = checker;
        this.action = action;
        this.args = args;
    }

    public String process(String str, RootIdentityContentHandler root) {
        return action.process(str, this);
    }

    public boolean check(String str) {
        return checker.check(str);
    }

    public boolean check(char[] context, int start, int length) {
            return checker.check( context, start, length);
    }

    public String contextAdjustment(char[] context, int start, int length){
        return action.contextAdjustment(context, start, length);
    }

    public IdentityAction getAction() {
        return action;
    }

    public IdentityActionType getType() {
        return action.getActionType();
    }

    @Override
    public String toString() {
        return String.format("Identity %s (%s %s)", checker.getClass().getName(), action.getClass().getName());
    }
}
