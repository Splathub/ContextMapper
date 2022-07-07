package Identity;

import Identity.Action.IdentityAction;
import Identity.Checker.IdentityChecker;

public class Identity {

    private String tag;
    private IdentityChecker checker;
    private IdentityAction action;


    public Identity(IdentityChecker checker, IdentityAction action, String tag, String[] args) {

    }

    public boolean checkedProcess(String context, StringBuffer buffer) {
        return false;
    }

    public boolean check(String context) {
        return false;
    }

    public void write(String context, StringBuffer buffer) {

    }

}
