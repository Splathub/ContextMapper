package identity.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;


public class Part {
    private final Logger LOG = LoggerFactory.getLogger(Part.class);

    private Identity[] identities;
    private Identity identity;
    private static final int defaultWindow = 3;
    private int window = 3;
    private int onPoint=0;

    public Part(Identity[] identities) {
        this.identities = identities;
        identity = identities[onPoint];
    }

    public void process(StringBuffer sb, RootIdentityContentHandler root) throws SAXException {
        for(int i=onPoint; i < identities.length && i <= onPoint + window; i++) {
            if (identities[i].check(sb, root)) {
                if (onPoint != i || identity == null) {
                    if (identity != null) {
                        finalProcess(root);
                    }
                    LOG.info("Identity change");
                    onPoint = i;
                    identity = identities[onPoint];
                }
                break;
            }
        }
        LOG.info("process identity " + onPoint + " : " + identity + "\n\t\t" + sb);
        identity.process(sb, this, root);
    }

    public void finalProcess(RootIdentityContentHandler root) throws SAXException {
        if (identity != null) {
            identity.finalProcess(root);
        }
    }

    public void pushPoint(int num) {
        onPoint += num;
        if(onPoint >= identities.length) {
            onPoint = identities.length-1;
        }
        else if (onPoint < 0) {
            onPoint = 0;
        }
        identity = identities[onPoint]; //TODO: rework, shouldn't set till checked
    }

    /**
     * Positive range includes the current onPoint, negative range starts back and doesn't include the past onPoint
     * @param range
     */
    public void adjustedRange(int range) {
        if (range < 0) {
            onPoint += range;
            window = range * -1;
            if (onPoint < 0) {
                onPoint = 0;
            }
        }
        else {
            window = range;
        }
    }

    public void includedProcess(int include, StringBuffer sb, RootIdentityContentHandler root) throws SAXException {
        pushPoint(include);
        identity.process(sb, this, root);
    }

    public static int getDefaultWindow() {
        return defaultWindow;
    }

    public int getRange(){
        return window;
    }

    public int getOnPoint() {
        return onPoint;
    }

}
