package identityMaster;

import identity.action.*;
import identityMaster.entity.Element;
import identityMaster.entity.IdentityKeeper;
import identityMaster.entity.IdentityMaster;
import java.util.AbstractMap.SimpleEntry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

import java.util.*;
import java.util.stream.Collectors;

public class IdentityMasterBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityMasterBuilder.class);

    private final IdentityMaster identityMaster;
    //private HeaderFooterBuilder hfBuilder;

    private Queue<SimpleEntry<String, org.jsoup.nodes.Element>> newRoots = new LinkedList<>();
    private List<String> textSlugs = new LinkedList<>();

    public static final String SPECIAL_NOTE_PREFIX = "#%";
    public static final Set<String> EMBEDDABLE = new HashSet(Arrays.asList("b", "u", "i", "sup", "sub", "em", "strong"));
    public static final Set<String> FONTS = new HashSet(Arrays.asList("font", "span"));

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
            newRoots.add(new SimpleEntry<>(new String(), e));
        }

        SimpleEntry<String, org.jsoup.nodes.Element> pair;
        Element rootParent;

        while(!newRoots.isEmpty()) {
            pair = newRoots.poll();
            rootParent = new Element();
            rootParent.setTag(pair.getValue().tagName().toLowerCase());
            conformAttributes(rootParent, pair.getValue().attributes());
            identitySelectionAndText(rootParent, pair.getValue());
            identityMaster.mergeElement(rootParent, rootParent.getSelfProxy(), pair.getKey());
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

        if (element.children().size() != 0) {
            for ( Node n : element.childNodes()) {

                if (n instanceof TextNode) {
                    TextNode t = (TextNode) n;
                    textSlugs.add(t.text());
                }
                else if (n instanceof org.jsoup.nodes.Element) {
                    org.jsoup.nodes.Element e = (org.jsoup.nodes.Element) n;

                    if (EMBEDDABLE.contains(e.tagName()) || FONTS.contains(e.tagName())) {

                        if (element.hasText() && !textSlugs.isEmpty()) {
                            ele.setIdentityName(ConjoinedIdentityAction.class.getName());
                            textSlugs.add(SPECIAL_NOTE_PREFIX+e.tagName()+":"+e.text()); //TODO: Assumes no inner text/nodes
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
                        textSlugs.add(SPECIAL_NOTE_PREFIX + "br");  //TODO: Assumes no inner text/nodes, shouldn't have

                    } else if (e.tagName().equalsIgnoreCase("table") ||
                            e.tagName().equalsIgnoreCase("tbody")) {
                        ele.setIdentityName(TableIdentityAction.class.getName());
                        processTable(ele, e);
                        //TODO: process info for tr, td, .. proxy
                        //newRoots.add(new SimpleEntry<>(getProxy(ele), e));

                    } else { // A Join/Wrap type; Div
                        ele.setIdentityName(JoinIdentityAction.class.getName());
                        newRoots.add(new SimpleEntry<>(getProxy(ele), e));
                    }
                }
            }
        }
        else {
            switch (element.tagName()) {
                case "a": //TODO: may have subs
                    ele.setIdentityName(AnchorIdentityAction.class.getName()); break;
                case "img":
                    ele.setIdentityName(ImageIdentityAction.class.getName()); break;
                default:
                    ele.setIdentityName(BaseIdentityAction.class.getName());
            }
        }

        if (ele.getIdentityName() == null) {
            ele.setIdentityName(BaseIdentityAction.class.getName()); // watch out, should be fine
        }

        ele.setTextSlugs(textSlugs);    // TODO: no text in a wrap needs to take first text and forward/push text with proxy~
        textSlugs = new LinkedList<>();
    }

    private void processTable(Element ele, org.jsoup.nodes.Element element) {
        Map<String, Set<String>> tableMapSS = new HashMap<>();
        Set<String> tempSet;
        Element temp = new Element();

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
                                newRoots.add(new SimpleEntry<>(getProxy(ele), (org.jsoup.nodes.Element)n));
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

    private String getProxy(Element e) {
        String proxy = e.getSelfProxy();
        if (proxy == null) {
            proxy = identityMaster.getProxy(e);
            e.setSelfProxy(proxy);
        }
        return proxy;
    }

    public IdentityMaster getIdentityMaster() {
        return identityMaster;
    }

    private void serializeYAML() throws IOException {
        Yaml yaml = new Yaml();
        Writer writer = new FileWriter(identityMaster.getPath()+".yaml");
        yaml.dump(identityMaster, writer);
    }

}
