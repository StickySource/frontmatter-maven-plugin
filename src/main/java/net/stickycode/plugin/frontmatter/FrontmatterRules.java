package net.stickycode.plugin.frontmatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FrontmatterRules
    implements Iterable<FrontmatterRule> {

  private List<FrontmatterRule> rules = new ArrayList<>();

  public void add(FrontmatterRule... rules) throws ThereCanOnlyBeOneAddRuleForEachKeyException {
    for (FrontmatterRule rule : rules) {
      if (this.rules.contains(rule))
        throw new ThereCanOnlyBeOneAddRuleForEachKeyException(rule);

      this.rules.add(rule);
    }
  }

  @Override
  public String toString() {
    if (rules.isEmpty())
      return "no rules";

    return rules.toString();
  }

  @Override
  public Iterator<FrontmatterRule> iterator() {
    return rules.iterator();
  }

}
