package net.stickycode.plugin.frontmatter;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrontmatterRulesExecution {

  private Pattern frontmatterParser = Pattern.compile("([a-zA-Z][a-zA-z0-9]*): .*");

  private List<String> keys = new ArrayList<>();

  private FrontmatterRules rules;

  public FrontmatterRulesExecution(FrontmatterRules rules) {
    this.rules = rules;
  }

  public void applyAdditions(BufferedWriter writer) {
    for (FrontmatterRule rule : rules) {
      if (!keys.contains(rule.getKey())) {
        rule.add(writer);
        keys.add(rule.getKey());
      }
    }
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

  @Override
  public String toString() {
    return rules.toString();
  }

}
