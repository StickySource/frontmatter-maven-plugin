package net.stickycode.plugin.frontmatter;

import java.io.BufferedWriter;

public interface FrontmatterRule {

  boolean allow(String key);

  void add(BufferedWriter writer);

  String getKey();

}
