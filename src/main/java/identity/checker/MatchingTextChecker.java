package identity.checker;

import identity.entity.RootIdentityContentHandler;

import java.util.Map;

public class MatchingTextChecker extends AbstractIdentityChecker {

    public MatchingTextChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
        return sb.toString().equals(getData("text"));
    }
}
