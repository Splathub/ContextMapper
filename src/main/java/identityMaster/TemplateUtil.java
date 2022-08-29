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
            default:
                return mkBaseWrapTemplate(keeper);
        }
    }

    public static String[] mkBaseWrapTemplate(IdentityKeeper keeper) {
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

        sb.append(">");
        template[0] = sb.toString();

        sb = new StringBuilder();
        sb.append("</").append(keeper.getTag()).append(">\n");
        template[1] = sb.toString();

        return template;
    }

    public static String[] mkTableTemplate(IdentityKeeper keeper) {
        String[] template = new String[2];
        //TODO: table tempalte cration segs
        return template;
    }
}
