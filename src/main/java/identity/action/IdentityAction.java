package identity.action;

import identity.entity.Identity;

public interface IdentityAction {

    IdentityActionType getActionType();

    String process(String context, Identity identity);

    String process(String context);

    String contextAdjustment(char[] ch, int start, int length);

    String stylize(String context);

}
