package Identity;

import Identity.action.IdentityAction;
import Identity.checker.IdentityChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Identity {
    private final Logger LOG = LoggerFactory.getLogger(Identity.class);

    private final String id;
    private final String attribute;
    private final IdentityAction action;
    private final IdentityChecker checker;
    private final Map<String, Object> args;


    public Identity(IdentityChecker checker, IdentityAction action, String id, Map<String, Object> args) {
        this.id = id;
        this.checker = checker;
        this.action = action;
        this.args = args;

        if (action.getAttribute() != null) {
            attribute = String.format(" id=\"%s\" style=\"%s\"", id, action.getAttribute());
        }
        else {
            attribute = String.format(" id=\"%s\"", id);
        }
    }

    public String checkedProcess(char[] context, int start, int length) {
        if (check(context, start, length)) {
            return attribute;
        }
        return null;
    }

    public boolean check(char[] context, int start, int length) {
            return checker.check( context, start, length);
    }

    public String contextAdjustment(char[] context, int start, int length){
        return action.contextAdjustment(context, start, length);
    }

    public String getAttribute(){
        return attribute;
    }

    @Override
    public String toString() {
        return String.format("Identity %s (%s %s)", id, checker.getClass().getName(), action.getClass().getName());
    }
}
