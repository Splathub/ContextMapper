package identityMaster.entity;

import identity.action.IdentityAction;
import identity.entity.Identity;

import java.io.Serializable;
import java.util.*;

public class Element implements Serializable {

    private String tag;
    private SortedMap<String, String> style = new TreeMap<>();
    private SortedMap<String, String> attributes = new TreeMap<>();
    private List<String> textSlugs = new LinkedList<>();
    private List<Element> children = new LinkedList<>();

    private String identityName;
    private Map<String, Object> args = new HashMap<>();

    private String selfProxy;

    public String getSSKeyInfo() {
        StringBuilder sb = new StringBuilder()
                .append(tag)
                .append(style)
                .append(attributes)
                .append(identityName);
        return sb.toString();
    }

    public void addToStyle(String key, String value) {
        if (style == null) {
            style = new TreeMap<>();
        }
        style.put(key, value);
    }

    public void addToAttributes(String key, String value) {
        if (attributes == null) {
            attributes = new TreeMap<>();
        }
        attributes.put(key, value);
    }

    public String getText() {
        return textSlugs.stream().reduce("", (tot, str) -> tot + str);
    }


    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public SortedMap<String, String> getStyle() {
        return style;
    }

    public void setStyle(SortedMap<String, String> style) {
        this.style = style;
    }

    public SortedMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(SortedMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getTextSlugs() {
        return textSlugs;
    }

    public void setTextSlugs(List<String> textSlugs) {
        this.textSlugs = textSlugs;
    }

    public String getSelfProxy() {
        return selfProxy;
    }

    public void setSelfProxy(String selfProxy) {
        this.selfProxy = selfProxy;
    }

    public List<Element> getChildren() {
        return children;
    }

    public void setChildren(List<Element> children) {
        this.children = children;
    }
}
