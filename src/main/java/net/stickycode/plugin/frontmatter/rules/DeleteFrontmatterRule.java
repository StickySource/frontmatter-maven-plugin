package net.stickycode.plugin.frontmatter.rules;

import java.io.BufferedWriter;
import java.util.Objects;

import net.stickycode.plugin.frontmatter.FrontmatterRule;

public class DeleteFrontmatterRule
    implements FrontmatterRule {

  private String key;

  public DeleteFrontmatterRule() {
  }

  public DeleteFrontmatterRule(String key) {
    this.key = Objects.requireNonNull(key, "Key must be non null");
  }

  @Override
  public boolean allow(String key) {
    return !key.equals(this.key);
  }

  @Override
  public void add(BufferedWriter writer) {
    // do nothing
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

}
