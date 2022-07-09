package Identity.action;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JoinIdentityActionTest {

    private String sentence = "This is a pretty cool sentence!";

    @Test
    public void AppendingParamCorrectly(){
        JoinIdentityAction action = new JoinIdentityAction();
        for (String str : sentence.split(" ")) {
            action.append(str + " ");
        }
        assertEquals(action.process(), sentence);
    }

}