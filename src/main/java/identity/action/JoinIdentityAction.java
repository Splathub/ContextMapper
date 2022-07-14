package identity.action;

/**
 * This class is a glorified StringBuffer class that may treat each appending differently through inheritance.
 */
public class JoinIdentityAction extends AbstractIdentityAction {
    protected StringBuffer sb;

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

        System.out.printf("\n\njoin \n\n\n\n\n\n%s\n\n\n\njoin\n\n",out);
        return out;
    }

}
