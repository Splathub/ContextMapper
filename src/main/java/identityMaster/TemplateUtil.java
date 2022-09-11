package identityMaster;

import identityMaster.entity.Element;
import identityMaster.entity.IdentityKeeper;

import java.util.Map;

public class TemplateUtil {

    public static String singleHeadTemplate(Element element) {
        if (element != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<").append(element.getTag());

            if (!element.getStyle().isEmpty()) {
                sb.append(element.getStyle());
            }

            if (!element.getAttributes().isEmpty()) {
                sb.append(element.getAttributes());
            }

            sb.append(">");
            return sb.toString();
        }
        return "";
    }

    public static String styleAttributesHTML(Element element) {
        if (element != null) {
            StringBuilder sb = new StringBuilder();

            if (!element.getStyle().isEmpty()) {
                sb.append(element.getStyle());
            }

            if (!element.getAttributes().isEmpty()) {
                sb.append(element.getAttributes());
            }
            return sb.toString();
        }
        return "";
    }

    public static String[] mkTemplate(IdentityKeeper keeper) {
        switch (keeper.getIdentityName()) {
            case "TableIdentityAction": //TODO: table tempalte segs
                return mkTableTemplate(keeper);
            case "AnchorIdentityAction":
                return mkAnchorWrapTemplate(keeper); // TODO: just test and check these names case
            default:
                return mkBaseWrapTemplate(keeper, false);
        }
    }

    public static String[] mkAnchorWrapTemplate(IdentityKeeper keeper) {
        return mkBaseWrapTemplate(keeper, true);
    }

    public static String[] mkBaseWrapTemplate(IdentityKeeper keeper, boolean openEnded) {
        String[] template = new String[2];

        StringBuilder sb = new StringBuilder()
                .append("<").append(keeper.getTag());

        if (!keeper.getStyle().isEmpty()) {
            sb.append(" STYLE=\"");
            for(Map.Entry<String, String> e : keeper.getStyle().entrySet()) {
                sb.append(e.getKey())
                        .append(": ")
                        .append(e.getValue())
                        .append("; ");
            }
            sb.append('"');
        }

        if (!keeper.getAttributes().isEmpty()) {
            for(Map.Entry<String, String> e : keeper.getAttributes().entrySet()) {
                sb.append(' ')
                        .append(e.getKey())
                        .append("=\"")
                        .append(e.getValue())
                        .append('"');
            }
        }

        if (!openEnded) {
            sb.append(">");
        }
        template[0] = sb.toString();

        sb = new StringBuilder();
        sb.append("</").append(keeper.getTag()).append(">\n");
        template[1] = sb.toString();

        return template;
    }

    public static String[] mkBaseWrapTemplate(Element element) {
        String[] template = new String[2];

        StringBuilder sb = new StringBuilder()
                .append("<").append(element.getTag());

        if (!element.getStyle().isEmpty()) {
            sb.append(" STYLE=\"");
            for(Map.Entry<String, String> e : element.getStyle().entrySet()) {
                sb.append(e.getKey())
                        .append(": ")
                        .append(e.getValue())
                        .append("; ");
            }
            sb.append('"');
        }

        if (!element.getAttributes().isEmpty()) {
            for(Map.Entry<String, String> e : element.getAttributes().entrySet()) {
                sb.append(' ')
                        .append(e.getKey())
                        .append("=\"")
                        .append(e.getValue())
                        .append('"');
            }
        }

        sb.append(">");
        template[0] = sb.toString();

        sb = new StringBuilder();
        sb.append("</").append(element.getTag()).append(">\n");
        template[1] = sb.toString();

        return template;
    }

    public static String[] mkTableTemplate(IdentityKeeper keeper) {
        String[] template = new String[2];
        //TODO: table tempalte cration segs
        return template;
    }
}
