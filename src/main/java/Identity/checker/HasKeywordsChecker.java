package Identity.checker;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HasKeywordsChecker extends AbstractIdentityChecker {

  private final Pattern pattern;

  public HasKeywordsChecker(Map<String, Object> data) {
    super(data);

    String[] keywords = (String[]) data.get("keywords");

    if(keywords == null )
      throw new RuntimeException(
              new IllegalArgumentException("keywords key is absent from data map passed to HasKeywordChecker"));

    String regex = Arrays.stream(keywords)
            .distinct()
            .map(keyword -> String.format("(?=.*%s)", keyword))
            .collect(Collectors.joining());

    this.pattern = Pattern.compile(regex);
  }

  @Override
  public boolean check(char[] context, int start, int length) {
    return pattern.matcher(new String(context, start, length)).find();
  }

  @Override
  public boolean check(String str) {
    return pattern.matcher(str).find();
  }

}
