package identityMaster.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class IdentityKeeper implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityKeeper.class);

    private String instructionalID;
    private List<Element> elements = new LinkedList<>();

    private String parentSSKey;
    private String rootParentSSKey;
    private int children = 0;
    private String StyleStrucKey;
    private String textKey;

    private SortedMap<String, Integer> keywordStats = new TreeMap<>();

    private String tag;
    private SortedMap<String, String> style = new TreeMap<>();
    private SortedMap<String, String> attributes = new TreeMap<>();

    private List<List<String>> textMarks = new LinkedList<>();

    private String identityName;
    private Map<String, Object> args = new HashMap<>();


    public IdentityKeeper(Element element) {
        if (element != null) {
            tag = element.getTag();
            style = element.getStyle();
            attributes = element.getAttributes();
            identityName = element.getIdentityName();
            args = element.getArgs();
            elements.add(element);
            textProcess(element);
            this.rootParentSSKey = element.getRootParentSSKey();
            this.parentSSKey = element.getParentSSKey();
        }
    }

   // public IdentityKeeper() { }


    public void addElement(Element element) {
        if (element != null) {
            if (canIncludeElement(element)) {
                elements.add(element);
                textProcess(element);

                if (rootParentSSKey == null && element.getRootParentSSKey() != null ||
                        rootParentSSKey != null && !rootParentSSKey.equalsIgnoreCase(element.getRootParentSSKey())) {
                    LOG.warn("Add element to IdentityKeeper with different root parents: "+ rootParentSSKey +"\n\t'"+ element.getRootParentSSKey() + "'\n\t"+element.getText());
                }

               // identitySelection(element);
            }
            else {
                //TODO: this will loop back
                //master.mergeElement(element);
                LOG.warn("Element not to be included: " + element);
            }
        }


        //TODO: update unionElement: if union can't include new element, Algorithm update/Key change and rebuild (expensive)
        //TODO: then check key for update
    }

    public void plusChild() {
        children++;
    }

    private void textProcess(Element element) {
        textMarks.add(element.getTextSlugs());
    }


    /**
     * If matched by ssKey, tag, style and attributes should match, children may not
     * @param element
     * @return
     */
    private boolean canIncludeElement(Element element) {
        if (element.getTag().equalsIgnoreCase(tag) &&
                element.getStyle().equals(style) &&
                element.getAttributes().equals(attributes)) {
            return true;
        }
        return false;
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


    public List<List<String>> getTextMarks() {
        return textMarks;
    }

    public void setTextMarks(List<List<String>> textMarks) {
        this.textMarks = textMarks;
    }

    public String getRootParentSSKey() {
        return rootParentSSKey;
    }

    public void setRootParentSSKey(String rootParentSSKey) {
        this.rootParentSSKey = rootParentSSKey;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getParentSSKey() {
        return parentSSKey;
    }

    public void setParentSSKey(String parentSSKey) {
        this.parentSSKey = parentSSKey;
    }

    public SortedMap<String, Integer> getKeywordStats() {
        return keywordStats;
    }

    public void setKeywordStats(SortedMap<String, Integer> keywordStats) {
        this.keywordStats = keywordStats;
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

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
