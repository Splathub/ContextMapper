package Identity.action;

/**
 * This class is a glorified StringBuffer class that may treat each appending differently through inheritance.
 */
public class JoinIdentityAction extends AbstractIdentityAction {

    private StringBuffer sb;

    public JoinIdentityAction() {
        super(IdentityActionType.ABSORB);
    }

    public void append(char[] ch, int start, int length) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append(ch, start, length);
    }

    public void append(String str) {
        if (sb == null) {
            sb = new StringBuffer();
        }
        sb.append(str);
    }

    public String process() {
        if (sb == null) {
            return "";
        }

        String out = sb.toString();
        sb = null;
        return out;
    }

}
