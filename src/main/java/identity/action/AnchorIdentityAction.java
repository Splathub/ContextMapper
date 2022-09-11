package identity.action;

import constants.Constants;
import identity.entity.GeneralContentHandler;
import identity.entity.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

public class AnchorIdentityAction extends BaseIdentityAction {
    private final Logger LOG = LoggerFactory.getLogger(AnchorIdentityAction.class);
    private String href;
    private boolean validLink;

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        if (!isStarted()) {
            setCorrectedLink(sb);
            if (validLink) {
                identity.getTemplateSegments()[0] = identity.getTemplateSegments()[0] + " href=\"" + href + "\">";
            }
            else {
                identity.getTemplateSegments()[0] = identity.getTemplateSegments()[0] + ">";
            }
        }
        super.process(sb, identity, handler);
    }

    private void setCorrectedLink(StringBuilder sb) {
        Matcher matcher = Constants.WEB_PATTERN.matcher(sb);
        if (sb != null || sb.length() > 3) {
            while (matcher.find()) {
                if (href == null) {
                    href = sb.substring(matcher.start(), matcher.end());
                    href = href.toLowerCase();
                }
                else {
                    LOG.warn("Multiple links found in single element, only first used");
                }
            }
        }
        if (href != null && href.contains("//www.sec.gov/")) {
            href = href.replaceFirst("https", "http");
            validLink = true;
        }
        else {
            validLink = false;
            LOG.warn("Got an invalid link!");
        }
    }
}
