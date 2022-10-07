package identity.entity;

public interface TextToAction {

    Identity identify(StringBuilder sb) throws CloneNotSupportedException;

    Identity getIdentityByKey(String key) throws CloneNotSupportedException;

    boolean isSameIdentity(String key, Identity identity);
}
