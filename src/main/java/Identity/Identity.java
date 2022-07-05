package Identity;

import Identity.Action.IdentityAction;
import Identity.Checker.IdentityChecker;

import java.util.Map;

public class Identity {

    private String name;
    private IdentityAction action;
    private IdentityChecker checker;
    private Map<String, String> args;

    public Identity(IdentityChecker checker, IdentityAction action, String name, Map<String, String> args) {
        this.args = args;
        this.name = name;
        this.action = action;
        this.checker = checker;
    }

    public boolean checkedProcess(String context, StringBuffer buffer) {
        return false;
    }

    public boolean check(String context) {
        return false;
    }

    public void write(String context, StringBuffer buffer) {

    }

    @Override
    public String toString() {
        return this.name;
    }
}
