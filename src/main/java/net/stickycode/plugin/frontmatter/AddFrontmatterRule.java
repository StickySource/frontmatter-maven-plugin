package net.stickycode.plugin.frontmatter;

import java.io.BufferedWriter;
import java.io.IOException;

public class AddFrontmatterRule
    implements FrontmatterRule {

  private String key;

  private String value;

  @Override
  public boolean allow(String key) {
    return true;
  }

  @Override
  public void add(BufferedWriter w) {
    try {
      w.write(key);
      w.write(": ");
      w.write(value);
      w.write("\n");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getKey() {
    return key;
  }

  public AddFrontmatterRule setKey(String key) {
    this.key = key;
    return this;
  }

  public String getValue() {
    return value;
  }

  public AddFrontmatterRule setValue(String value) {
    this.value = value;
    return this;
  }

}
