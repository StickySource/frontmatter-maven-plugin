package net.stickycode.plugin.frontmatter;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrontmatterRules {

  private Pattern frontmatterParser = Pattern.compile("([a-zA-Z][a-zA-z0-9]*): .*");

  private List<FrontmatterRule> rules = new ArrayList<>();

  private List<String> keys = new ArrayList<>();

  public void applyAdditions(BufferedWriter writer) {
    for (FrontmatterRule rule : rules) {
      rule.add(writer);
    }
  }

  public CharSequence transform(String line) {
    return line;
  }

  public boolean allow(String line) {
    Matcher matcher = frontmatterParser.matcher(line);
    if (!matcher.matches())
      return true;

    String key = matcher.group(1);
    if (keys.contains(key))
      return false;

    keys.add(key);
    for (FrontmatterRule rule : rules)
      if (!rule.allow(key))
        return false;

    return true;
  }

  String parseFrontmatterKey(String line) {
    return line;
  }

  public void add(FrontmatterRule... rules) {
    for (FrontmatterRule rule : rules) {
      this.rules.add(rule);
    }
  }

}
