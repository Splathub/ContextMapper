package Identity.action;

public interface IdentityAction {

    String getAttribute();

    String stylize(String context);

    String contextAdjustment(char[] ch, int start, int length);

}
