package identity.action;

import identity.entity.GeneralContentHandler;
import identity.entity.Identity;

/**
 * This class is the base level for other Actions as needed.
 */
public class BaseIdentityAction implements IdentityAction {

    private boolean started = false;

    @Override
    public void process(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        if (!started) {
            handler.write(identity.getTemplateSegments()[0]);
            started = true;
        }
        handler.write(sb.toString());
    }

    @Override
    public void endProcess(StringBuilder sb, Identity identity, GeneralContentHandler handler) {
        if (!started) {
            handler.write(identity.getTemplateSegments()[0]);
        }
        started = false;
        handler.write(sb.toString());
        handler.write(identity.getTemplateSegments()[1]);
    }

    public boolean isStarted() {
        return started;
    }

}
