package identity.checker;

import identity.exception.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HasKeywordsChecker extends AbstractIdentityChecker {
  private final Logger LOG = LoggerFactory.getLogger(HasKeywordsChecker.class);

  private final Pattern pattern;

  public HasKeywordsChecker(Map<String, Object> data) {
    super(data);

    ArrayList<String> keywords = (ArrayList<String>) data.get("keywords");

    if (keywords == null) {
      throw new ParameterException("HasKeywordsChecker Key: 'keywords' is invalid");
    }

    String regex = keywords.stream()
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
