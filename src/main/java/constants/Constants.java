package constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class Constants {

    public static final String IDENTITY_MAPS_PATH = "identity/rootMaps/";
    public static final String IDENTITY_MODELS_PATH = "identity/models/";

    //Args keys
    public static final String PARENT = "PARENT";
    public static final String EMBEDDED_STYLE_ID = "EMBEDDED_STYLE_ID";
    public static final String TABLE_ROW = "TABLE_ROW";
    public static final String TABLE_COL = "TABLE_COL";
    public static final String TABLE_HEADER = "TABLE_HEADER";
    public static final String TABLE_FOOTER = "TABLE_FOOTER";

    public static final String SPECIAL_NOTE_PREFIX = "~Pre_uqzqy"; //#%
    public static final String SPECIAL_NOTE_POSTFIX = "~Post_uqzqy";

    public static final String SEC_WEB_DOMAIN = "http://www.sec.gov/";
    public static final String REGEX_WEB_SELECT = "(?i)(http|www)(.*?)(?=\\s|$)";
    public static final Pattern WEB_PATTERN = Pattern.compile(REGEX_WEB_SELECT);
    public static final String REGEX_REMOVE = "^"+Constants.SPECIAL_NOTE_PREFIX+//"(.*?):|" +
            "^"+Constants.SPECIAL_NOTE_POSTFIX +
            "|\\.|,|\"|\\(|\\)|:";
    public static final Set<String> REMOVE = new HashSet(Arrays.asList(" ", "")); // empty string?

    public static final Set<String> EMBEDDABLE = new HashSet(Arrays.asList("b", "u", "i", "sup", "sub", "em", "strong"));
    public static final Set<String> FONTS = new HashSet(Arrays.asList("font", "span"));


    // Used to write
    public static final String FILLER = "&nbsp;";
}
