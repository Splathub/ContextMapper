package Identity.action;

/**
 * This class may make changes to given context based logic. On a base level, no changes.
 */
public abstract class ReplaceIdentityAction extends AbstractIdentityAction {

    public ReplaceIdentityAction() {
        super(IdentityActionType.REPLACE);
    }

}
