package net.stickycode.plugin.frontmatter;

import java.io.BufferedWriter;
import java.io.IOException;

public enum MarkdownProcessor {
  Pending {
    @Override
    MarkdownProcessor consume(String line, BufferedWriter writer, FrontmatterRules rules) throws IOException {
      writer.append(line).append("\n");
      if ("---".equals(line))
        return MarkdownProcessor.Frontmatter;

      return this;
    }
  },
  Frontmatter {
    @Override
    MarkdownProcessor consume(String line, BufferedWriter writer, FrontmatterRules rules) throws IOException {
      if ("---".equals(line)) {
        rules.applyAdditions(writer);
        writer.append(line).append("\n");
        return MarkdownProcessor.Body;
      }

      if (rules.allow(line))
        writer.append(rules.transform(line)).append("\n");
      return this;
    }
  },
  Body;

  MarkdownProcessor consume(String line, BufferedWriter writer, FrontmatterRules rules) throws IOException {
    writer.append(line).append("\n");
    return this;
  }
}
