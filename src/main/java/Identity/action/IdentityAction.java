package Identity.action;

public interface IdentityAction {

    IdentityActionType getActionType();

    String contextAdjustment(char[] ch, int start, int length);

    String stylize(String context);

}
