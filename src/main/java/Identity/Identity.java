package Identity;

import Identity.action.IdentityAction;
import Identity.checker.IdentityChecker;

public class Identity {

    private final String id;
    private final IdentityChecker checker;
    private final IdentityAction action;
    private final String attribute;
    //TODO: parameters as map


    public Identity(IdentityChecker checker, IdentityAction action, String id) {
        this.id = id;
        this.checker = checker;
        this.action = action;
        attribute = buildAttribute();
    }

    public String checkedProcess(char[] context, int start, int length) {
        if (checker.check(context, start, length)) {
            return attribute;
        }
        return null;
    }

    public boolean check(char[] context, int start, int length) {
        return checker.check(context, start, length);
    }

    public String stylize(String context) {
        return action.stylize(context);
    }

    public String getAttribute(){
        return attribute;
    }

    public String contextAdjustment(char[] context, int start, int length){
        return action.contextAdjustment(context, start, length);
    }

    private String buildAttribute() {
        if (action.getAttribute() != null) {
            return String.format(" id=\"%s\" style=\"%s\"", id, action.getAttribute());
        }
        return String.format(" id=\"%s\"", id);
    }

}
