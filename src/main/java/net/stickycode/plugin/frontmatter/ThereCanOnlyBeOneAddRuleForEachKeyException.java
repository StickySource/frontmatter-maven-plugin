package net.stickycode.plugin.frontmatter;

@SuppressWarnings("serial")
public class ThereCanOnlyBeOneAddRuleForEachKeyException
    extends RuntimeException {

  public ThereCanOnlyBeOneAddRuleForEachKeyException(FrontmatterRule rule) {
    super("Tried to use to 'Add' rules for key '" + rule.getKey() + "', there can be only one");
  }

}
