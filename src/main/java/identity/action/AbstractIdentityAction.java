package identity.action;

public abstract class AbstractIdentityAction implements IdentityAction {

    private final IdentityActionType type;

    public AbstractIdentityAction(IdentityActionType type){
        this.type = type;
    }

    @Override
    public String contextAdjustment(char[] ch, int start, int length) {
        return new String(ch);
    }

    @Override
    public String stylize(String context) {
        return context;
    }

    @Override
    public IdentityActionType getActionType() {
        return type;
    }

}
