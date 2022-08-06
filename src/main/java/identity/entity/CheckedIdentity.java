package identity.entity;

import identity.action.IdentityAction;
import identity.checker.IdentityChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * This class is responsible for identifying context as a String or char[] by the parameters of the IdentityChecker. If
 * found TRUE the IdentityAction will preform it's changes if any. When identified, the attribute would be used to
 * assign the context by the ContentHandler.
 */
public class CheckedIdentity extends Identity {
    private final Logger LOG = LoggerFactory.getLogger(CheckedIdentity.class);
    private final IdentityChecker checker;


    public CheckedIdentity(IdentityChecker checker, IdentityAction action, String template, Map<String, Object> args) {
        super(action, template, args);
        this.checker = checker;
    }

    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        return checker.check(sb, root);
    }

    @Override
    public String toString() {
        return String.format("CheckedIdentity (%s %s)", checker.getClass().getName(), super.getAction().getClass().getName());
    }

}
