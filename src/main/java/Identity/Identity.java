package Identity;

import Identity.Action.IdentityAction;
import Identity.Checker.IdentityChecker;

public class Identity {

    private String name;
    private String[] args;
    private IdentityAction action;
    private IdentityChecker checker;

    public Identity(IdentityChecker checker, IdentityAction action, String name, String[] args) {
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
        System.out.println(this.name);
        System.out.println(this.args);
        return this.name;
    }
}
