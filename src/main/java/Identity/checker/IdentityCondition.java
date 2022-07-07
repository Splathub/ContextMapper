package Identity.checker;

import java.util.HashMap;
import java.util.Map;

public class IdentityCondition {

    private static final Map<String, Object> map = new HashMap<>(9);



    public static boolean keyword(char[] context, int start, int length) {
        int j;
        String keyword = (String)(map.get("keyword"));

        for (int i=start; i<start+length; i++) {
            if (context[i] == keyword.charAt(0)) {
                j=1;
                while (context[i+j] == keyword.charAt(j) && j < keyword.length()) {
                    j++;
                    if (j == keyword.length()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean is(char[] context, int start, int length) {
        int j=0;
        String keyword = (String)(map.get("keyword"));

        for (int i=start; i<start+length; i++) {
            if (context[i] != keyword.charAt(j++)) {
                return false;
            }
        }
        return true;
    }

    public static IdentityChecker tesCK = ((context, start, length) -> {
       System.out.println("Has: "+map.size());
        return false;
    });

    public static IdentityChecker getTestCk(){
        return tesCK;
    }

    public static boolean test(char[] context, int start, int length) {
        System.out.println("In test method");
        System.out.println("got:" + map.size());
        return false;
    }

}
