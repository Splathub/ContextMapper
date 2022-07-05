package Identity.action;

public interface IdentityAction {

    String getAttribute();

    String contextAdjustment(char[] ch, int start, int length);

    String stylize(String context);

}
