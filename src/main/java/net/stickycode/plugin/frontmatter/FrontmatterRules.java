package net.stickycode.plugin.frontmatter;

import java.io.BufferedWriter;

public class FrontmatterRules {

  public void applyAdditions(BufferedWriter writer) {
  }

  public CharSequence transform(String line) {
    return line;
  }

  public boolean allow(String line) {
    return true;
  }

}
