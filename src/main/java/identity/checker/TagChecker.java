package identity.checker;

import identity.entity.RootIdentityContentHandler;

import java.util.Map;


public class TagChecker extends AbstractIdentityChecker {

    public TagChecker(Map<String, Object> data) {
        super(data);
    }

    @Override
    public boolean check(StringBuffer sb, RootIdentityContentHandler root) {
       return root.inTag( (String) getData("tag"));
    }
}
