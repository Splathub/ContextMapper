package identityMaster.entity;

import identity.entity.Identity;
import identityMaster.KeyGenerator;

import java.util.List;

public class IdentityKeeper {

    private String instructionalID;
    private final KeyGenerator keyGenerator;
    private String StyleStrucKey;
    private String textKey;

    private Element unionElement;
    private List<Element> elements;
    private Identity identity;


    public IdentityKeeper(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public void addElement(Element element) {

        //TODO: update unionElement: if union can't include new element, Algorithm update/Key change and rebuild (expensive)
        //TODO: then check key for update
        elements.add(element);
    }



    public String getInstructionalID() {
        return instructionalID;
    }

    public void setInstructionalID(String instructionalID) {
        this.instructionalID = instructionalID;
    }

    public String getStyleStrucKey() {
        return StyleStrucKey;
    }

    public void setStyleStrucKey(String styleStrucKey) {
        StyleStrucKey = styleStrucKey;
    }

    public String getTextKey() {
        return textKey;
    }

    public void setTextKey(String textKey) {
        this.textKey = textKey;
    }

    public Element getUnionElement() {
        return unionElement;
    }

    public void setUnionElement(Element unionElement) {
        this.unionElement = unionElement;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }
}
