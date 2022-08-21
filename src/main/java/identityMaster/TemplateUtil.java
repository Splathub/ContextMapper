package identityMaster;

import identityMaster.entity.Element;
import identityMaster.entity.IdentityKeeper;

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
            case "TableIdentityAction":
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
            sb.append(keeper.getStyle());
        }

        if (!keeper.getAttributes().isEmpty()) {
            sb.append(keeper.getAttributes());
        }

        sb.append(">");
        template[0] = sb.toString();

        sb = new StringBuilder();
        sb.append("</").append(keeper.getTag()).append(">\n");

        return template;
    }

    public static String[] mkTableTemplate(IdentityKeeper keeper) {
        String[] template = new String[2];
        //TODO:
        return template;
    }
}
