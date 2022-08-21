package identityMaster;

import java.io.Serializable;

public class HeaderFooterBuilder implements Serializable {

    private String[] headers;
    private String[] footer;
    private String pattern;

    public void addHeader(String header) {

    }

    public void addFooter(String footer) {

    }

    private void identifyPattern() {

    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String[] getFooter() {
        return footer;
    }

    public void setFooter(String[] footer) {
        this.footer = footer;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
