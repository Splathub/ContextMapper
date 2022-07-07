import Identity.Identity;
import Identity.action.IdentityAction;
import Identity.checker.IdentityCondition;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class ContextMapperProcess {


    final static String SAMPLE_PDF = "src/test/resources/samples/sample2.pdf";

    public static void main (String[] args) throws NoSuchMethodException, NoSuchFieldException {

        char[] c = new char[0];
        //, new Class[]{c.getClass(), int.class, int.class}


        Identity identity = new Identity(
                IdentityCondition.tesCK,
                new IdentityAction() {
                    @Override
                    public String getAttribute() {
                        return null;
                    }

                    @Override
                    public String contextAdjustment(char[] ch, int start, int length) {
                        return null;
                    }

                    @Override
                    public String stylize(String context) {
                        return null;
                    }
                },
                "test",
                new HashMap<>()
        );


        System.out.println(identity.check(null, 0, 0));


       // ContextMapper cm = new ContextMapper(SAMPLE_PDF, null);
       // System.out.println(cm.process());

    }
}
