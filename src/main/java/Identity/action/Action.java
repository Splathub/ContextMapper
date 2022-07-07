package Identity.action;

import java.nio.Buffer;

public class Action implements IdentityAction {
    public Action(String name){

    }

    @Override
    public String getAttribute() {
        return null;
    }

    @Override
    public String contextAdjustment(char[] ch, int start, int length) {
        return null;
    }

    @Override
    public String stylize(String context) {
        return null;
    }
}
