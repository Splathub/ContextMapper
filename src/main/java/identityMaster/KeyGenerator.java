package identityMaster;

import constants.Constants;
import identityMaster.entity.Element;
import identityMaster.entity.IdentityKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyGenerator implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(KeyGenerator.class);
    private final List<String> STOP_WORDS;

    private String algorithm;


    public KeyGenerator() throws IOException {
        STOP_WORDS = Files.readAllLines(Paths.get("src/main/resources/stop_words_english.txt"));
    }


    public String generateStyleStrucKey(Element element) {
        if (element == null) {
            return null;
        }
        String ssKey = element.getSSKeyInfo().replaceAll(" ", "").toLowerCase();
        if (ssKey.isEmpty()) {
            return null;
        }
        return ssKey;
    }

    public String generateStyleStrucKey(String[] style, String[] action, String tag) {
        return "";
    }

    public String generateStyleStrucKey(IdentityKeeper identityKeeper) {
        return "";
    }

    public String generateContextKey(StringBuilder text) {
        return "";
    }

    public List<String> generateContextKey(IdentityKeeper identityKeeper) {
        List<String> keys = new LinkedList<>();
        Map<String, Integer[]> wordSpan = new HashMap<>();
        int size = identityKeeper.getTextMarks().size();
        Integer[] values;
        int i = 1;

        for(List<String> chunks : identityKeeper.getTextMarks()) {
            Set<String> cleanTokens = cleanText(chunks);

            for(String token : cleanTokens){
                values = wordSpan.get(token);
                if (values == null) {
                    values = new Integer[size+1];
                    Arrays.fill(values, 0);
                }
                values[0]++;
                values[i]=1;
                wordSpan.put(token, values);
            }

            i++;
        }

        /*
        LOG.info("Size: " + size + "-------------------");
        for(Map.Entry<String, Integer[]> entry : wordSpan.entrySet()) {
            LOG.info(entry.getValue()[0] + " : " + entry.getKey());
        }
        LOG.info("-------------------------------------");
*/



        /*
        identityKeeper.getTextMarks().stream()
                .flatMap(list -> list.stream()
                    .flatMap(chunk -> Stream.of(chunk.split(" ")))
                        .collect(Collectors.toSet())
                        .)
                .collect(Collectors.toList());
        */
        return keys;
    }



    public Set<String> cleanText(List<String> textChunks) {
        Set<String> tokens = textChunks.stream()
                .flatMap(line -> Stream.of( line.toLowerCase().split(" "))
                        .map( s -> s.replaceAll( Constants.REGEX_REMOVE, "")))
                .collect(Collectors.toSet());
       // tokens.removeAll(STOP_WORDS);

        return tokens;
    }

    public String generateContextKey(Element element) {
        if (element == null) {
            return "";
        }
        return element.getText();
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }


}
