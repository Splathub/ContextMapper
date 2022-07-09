package Identity.action;

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
        String out = sb.toString();
        sb = null;
        return out;
    }

}
