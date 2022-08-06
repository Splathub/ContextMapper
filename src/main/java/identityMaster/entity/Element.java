package identityMaster.entity;

import java.util.Map;

public class Element {

    private String tag;
    private Map<String, String> style;
    private Map<String, String> attributes;
    private String text;
    private Element[] children;



    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, String> getStyle() {
        return style;
    }

    public void setStyle(Map<String, String> style) {
        this.style = style;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Element[] getChildren() {
        return children;
    }

    public void setChildren(Element[] children) {
        this.children = children;
    }
}
