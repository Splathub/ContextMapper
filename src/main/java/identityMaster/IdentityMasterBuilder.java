package identityMaster;

import constants.Constants;
import identity.action.AnchorIdentityAction;
import identity.action.BaseIdentityAction;
import identity.action.ImageIdentityAction;
import identity.action.TableIdentityAction;
import identityMaster.entity.Element;
import identityMaster.entity.IdentityKeeper;
import identityMaster.entity.IdentityMaster;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.AbstractMap.SimpleEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static identityMaster.TemplateUtil.mkBaseWrapTemplate;

public class IdentityMasterBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityMasterBuilder.class);

    private final IdentityMaster identityMaster;
    private Queue<SimpleEntry<Element, org.jsoup.nodes.Element>> toBuildIdentities = new LinkedList<>();
    private List<String> textSlugs = new LinkedList<>();


    public IdentityMasterBuilder() throws IOException {
        identityMaster = new IdentityMaster("tempIdentityMaster");
        //hfBuilder = new HeaderFooterBuilder();
    }

    public IdentityMasterBuilder(String name) throws IOException {
        identityMaster = new IdentityMaster(name);
        //hfBuilder = new HeaderFooterBuilder();
    }

    public IdentityMaster buildFromDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            int i = 1;
            for(File file : Objects.requireNonNull(directory.listFiles())) {
                LOG.info("Start building file " + i);
                process(file);
                LOG.info("Finished building file " + i);
                i++;
            }
        }
        else {
            throw new RuntimeException("Not a directory");
        }

        serializeYAML();
        identityMaster.generateAllTextKeys(); //TODO: temp
        LOG.info("IdentityMaster HTML finished");
        return identityMaster;
    }

    public IdentityMaster build(File html) throws IOException {
        if (!html.isFile()) {
            throw new RuntimeException("Not a file");
        }
        process(html);

        serializeYAML();
        identityMaster.generateAllTextKeys(); //TODO: temp
        LOG.info("IdentityMaster HTML finished");
        return identityMaster;
    }

    /*

    public void build() throws IOException {
        LOG.info("IdentityMaster HTML parsing");
        Document doc = Jsoup.parse(html);
        LOG.info("IdentityMaster HTML parsing successful");

        for (org.jsoup.nodes.Element e : doc.body().children()) {
            recursiveBuild(e);
        }
        LOG.info("IdentityMaster HTML IdentityKeepers added");

        serializeYAML();
        LOG.info("IdentityMaster HTML finished");
    }

    private void recursiveBuild(org.jsoup.nodes.Element parentElement) {
        Element element;
        for (org.jsoup.nodes.Element e : parentElement.children()) {
            element = new Element();
            conformAttributes(rootParent, e.attributes());
            identitySelection(rootParent, e);
            identityMaster.mergeElement(rootParent);
        }
    }*/

    private void process(File html) throws IOException {
        if (!html.isFile()) {
            throw new RuntimeException("Not a file");
        }


        LOG.info("IdentityMaster HTML parsing");
        Document doc = Jsoup.parse(html);
        LOG.info("IdentityMaster HTML parsing successful");

        for (org.jsoup.nodes.Element e : doc.body().children()) {
            toBuildIdentities.add(new SimpleEntry<>(new Element(), e));
        }

        SimpleEntry<Element, org.jsoup.nodes.Element> pair;
        Element element;
        while(!toBuildIdentities.isEmpty()) {
            pair = toBuildIdentities.poll();
            element = pair.getKey();
            element.setTag(pair.getValue().tagName().toLowerCase());
            conformAttributes(element, pair.getValue().attributes());
            identitySelectionAndText(element, pair.getValue());
            identityMaster.mergeElement(element);
        }

            /*if (superParent == null) {
                current = new Element();
                current.setTag(e.tagName());
                conformAttributes(current, e.attributes());

                if (e.hasText()) {
                    current.setTextSlugs(e.textNodes().stream()
                            .map(TextNode::text)
                            .collect(Collectors.toList()));
                    identityMaster.mergeElement(current);
                }
                else {
                    superParent = current;
                    children = new LinkedList<>();
                }
            }
            else {
                if (EMBEDDABLE.contains(e.tagName())) {

                }


                if (e.hasText()) {
                    current.setText(e.ownText());
                    if (current == superParent) {
                        identityMaster.mergeElement(superParent);
                        superParent = null;
                    }
                }
                else {

                }
            }*/

        LOG.info("IdentityMaster HTML IdentityKeepers added");
    }

    private void conformAttributes(Element element, Attributes attributes) {
        if (!(attributes == null && attributes.isEmpty())) {
            SortedMap<String, String> atts = new TreeMap<>();
            for (Attribute a : attributes.asList()) {
                if (a.getKey().equalsIgnoreCase("style")) {
                    SortedMap<String, String> style = new TreeMap<>();
                    String[] entry;
                    for (String str : a.getValue().split(";")) {
                        entry = str.split(":");
                        if (entry.length > 1) {
                            style.put(entry[0].trim(), entry[1].trim());
                        }
                    }
                    element.setStyle(style);
                } else {
                    atts.put(a.getKey().trim(), a.getValue().trim());
                }
            }
            element.setAttributes(atts);
        }
    }

    private void createChildren(Element rootParentChildren, org.jsoup.nodes.Element parent) {
        Stack<SimpleEntry<Element, org.jsoup.nodes.Element>> stack = new Stack<>();
        SimpleEntry<Element, org.jsoup.nodes.Element> current = new SimpleEntry<>(rootParentChildren, parent);
        stack.push(current);

        //LOG.info(parent.getAllElements().stream().map(org.jsoup.nodes.Element::tagName).reduce("", (oldV, newV) -> oldV + ","+newV));

        while (!stack.empty()) {
            current = stack.pop();
            Element element = current.getKey();
            org.jsoup.nodes.Element node = current.getValue();

            element.setTag(node.tagName());
            conformAttributes(element, node.attributes());
            element.setTextSlugs(node.textNodes().stream()
                    .map(TextNode::text)
                    .collect(Collectors.toList()));

            List<Element> children = new LinkedList<>();
            for (org.jsoup.nodes.Element c : current.getValue().children()) {
                Element child = new Element();
                children.add(child);
                stack.push(new SimpleEntry<>(child, c));
            }
            element.setChildren(children);
        }
    }

    public void identitySelectionAndText(Element ele, org.jsoup.nodes.Element element) {
        switch (element.tagName()) {
            case "a": //TODO: may have subs
                ele.setIdentityName(AnchorIdentityAction.class.getName()); break;
            case "img":
                ele.setIdentityName(ImageIdentityAction.class.getName()); break;
            case "table":
            case "tbody":
                ele.setIdentityName(TableIdentityAction.class.getName());
                processTable(ele, element); //TODO: process info for tr, td, .. proxy
                break;
            default:
                ele.setIdentityName(BaseIdentityAction.class.getName());
        }

        List<String[]> embeddedStyleInOrderList = new LinkedList<>(); // of Start and End tag template, push to table for it's text pins
        if (element.children().size() != 0 &&
                !ele.getIdentityName().equalsIgnoreCase(TableIdentityAction.class.getName())) {

            for (Node n : element.childNodes()) {
                if (n instanceof TextNode) {
                    TextNode t = (TextNode) n;
                    textSlugs.add(t.text());
                } else if (n instanceof org.jsoup.nodes.Element) {
                    org.jsoup.nodes.Element e = (org.jsoup.nodes.Element) n;

                    if (Constants.EMBEDDABLE.contains(e.tagName()) || Constants.FONTS.contains(e.tagName())) {

                        if (element.hasText() && !textSlugs.isEmpty()) {
                            embeddedStyleInOrderList.add(mkBaseWrapTemplate(ele)); //TODO: fix, getting Ps
                            textSlugs.add(" "+Constants.SPECIAL_NOTE_PREFIX+" "+e.text()+" "+Constants.SPECIAL_NOTE_POSTFIX); // Add pins;
                            //TODO; may need a new pin type string

                        } else {
                            switch (e.tagName()) {
                                case "b":
                                case "strong":
                                    ele.addToStyle("font-weight", "bold");
                                    break;
                                case "i":
                                case "em":
                                    ele.addToStyle("font-style", "italic");
                                    break;
                                case "u":
                                    ele.addToStyle("text-decoration", "underline");
                                    break;
                                case "sup":
                                    ele.addToStyle("vertical-align", "super");
                                    break;
                                case "sub":
                                    ele.addToStyle("vertical-align", "sub");
                                    break;
                                case "font":
                                case "span":
                                    for (Attribute a : e.attributes().asList()) {
                                        if (a.getKey().equalsIgnoreCase("style")) {
                                            String[] entry;
                                            for (String str : a.getValue().split(";")) {
                                                entry = str.split(":");
                                                if (entry.length > 1) {
                                                    ele.addToStyle(entry[0].trim(), entry[1].trim());
                                                }
                                            }
                                        } else {
                                            ele.addToAttributes(a.getKey().trim(), a.getValue().trim());
                                        }
                                    }
                                    break;
                                default:
                                    LOG.warn("Tag without process for whole text process: " + e.tagName() + "\n" + e.textNodes());
                            }
                        }

                    } else if (e.tagName().equalsIgnoreCase("br")) {
                        textSlugs.add(Constants.SPECIAL_NOTE_PREFIX + "br");  //TODO: Assumes no inner text/nodes, shouldn't have

                    } else { // sub elements children
                        appendNewPendingIdentity(ele, e);
                    }
                }
            }
        }

        ele.setTextSlugs(textSlugs);    // TODO: no text in a wrap needs to take first text and forward/push text with proxy~
        textSlugs = new LinkedList<>();

        if (!embeddedStyleInOrderList.isEmpty()) {
            ele.getArgs().put(Constants.EMBEDDED_STYLE_ID, identityMaster.getEmbeddedTextStyleID(ele.getText(), embeddedStyleInOrderList));
        }
    }

    private void appendNewPendingIdentity(Element parentElement, org.jsoup.nodes.Element childElement) {
        Element child = new Element();
        String ssKey = identityMaster.getSSKey(parentElement);
        child.setParentSSKey(ssKey);
        if (parentElement.getRootParentSSKey() == null) {
            child.setRootParentSSKey(ssKey);
        }
        else {
            child.setRootParentSSKey(parentElement.getRootParentSSKey());
        }
        toBuildIdentities.add(new SimpleEntry<>(child, childElement));
    }

    private void processTable(Element ele, org.jsoup.nodes.Element element) {
        Map<String, Set<String>> tableMapSS = new HashMap<>();
        Set<String> tempSet;
        Element temp;

        for(org.jsoup.nodes.Element e : element.children()) {
            if (e.tagName().equalsIgnoreCase("tr")) {
                temp = new Element();
                conformAttributes(temp, e.attributes());

                tempSet = tableMapSS.getOrDefault("tr", new LinkedHashSet<>());
                tempSet.add(TemplateUtil.styleAttributesHTML(temp));
                tableMapSS.put("tr", tempSet);

                for (org.jsoup.nodes.Element trE : e.children()) {
                    if (trE.tagName().equalsIgnoreCase("td")) {
                        temp = new Element();
                        conformAttributes(temp, trE.attributes());

                        tempSet = tableMapSS.getOrDefault("td", new LinkedHashSet<>());
                        tempSet.add(TemplateUtil.styleAttributesHTML(temp));
                        tableMapSS.put("td", tempSet); //TODO: table resume and build args

                        for (Node n : trE.childNodes()) {
                            if (n instanceof TextNode) {
                                //TextNode t = (TextNode) n;
                                //textSlugs.add(t.text());
                                //LOG.warn("Table TD has off-placed Text, not processed"); TODO: handle
                            } else if (n instanceof org.jsoup.nodes.Element) {
                                appendNewPendingIdentity(ele, (org.jsoup.nodes.Element)n);
                            }
                        }
                    }
                    else {
                        LOG.warn("TD else ignored: " + trE.tagName());
                    }
                }
            }
            else if (e.tagName().equalsIgnoreCase("tbody")) {
                /*temp = new Element();
                conformAttributes(temp, e.attributes());

                tempSet = tableMapSS.getOrDefault("tbody", new LinkedHashSet<>());
                tempSet.add(TemplateUtil.styleAttributesHTML(temp));
                tableMapSS.put("tbody", tempSet);*/
                //LOG.warn("Ignored tbody element"); TODO: handle?
            }
            else {
               LOG.warn("TR else ignored: " + e.tagName());
               //newRoots.add(new SimpleEntry<>(proxy, e));
                //identitySelectionAndText(temp, e);
            }
        }


        Map<String, Object> args = new HashMap<>();
        for(Map.Entry<String, Set<String>> e: tableMapSS.entrySet()) {
            String tag = e.getKey();
            Set<String> setTemplates = e.getValue();

            if (setTemplates.isEmpty()) {
                args.put(tag, "<"+tag+">");
            }
            else {
                //TODO: find pattern, len1=same,
                // first diff to next following = header,
                // last differ from post = footer,
                // inner row change of len4min = row cycle/ with head and footer/extended headfoot

                final String[] last = {null};
                final boolean[] firstDiffer = new boolean[1];
                final boolean[] cycleRow = new boolean[1];
                final boolean[] lastDiffer = new boolean[1];
                final int[] i = {0};

                setTemplates.forEach(s -> {
                            if(last[0] == null) {
                                last[0] = s;
                            }
                            else if(!last[0].equalsIgnoreCase(s)) {
                                if (i[0] ==1) {
                                    firstDiffer[0] = true;
                                }
                                else if (i[0] == setTemplates.size()-1) {
                                    lastDiffer[0] = true;
                                }
                                else if(setTemplates.size() >= 4) {
                                    cycleRow[0] = true;
                                }
                            }
                            i[0]++;
                        }
                );

                args.put(tag, last[0]);
            }
        }
        ele.setArgs(args);

    }

    private void generateTextKeys() {
        String ssKey;
        IdentityKeeper keeper;
        for(Map.Entry<String, IdentityKeeper> entry : identityMaster.getsKHash().entrySet()) {
            ssKey = entry.getKey();
            keeper = entry.getValue();



        }
    }

    public IdentityMaster getIdentityMaster() {
        return identityMaster;
    }

    private void serializeYAML() throws IOException {
        Yaml yaml = new Yaml();
        String path = identityMaster.getPath()+".yaml";
        Writer writer = new FileWriter(path);
        yaml.dump(identityMaster, writer);
        LOG.info("Created IdentityMaster: " + path);
    }

}
