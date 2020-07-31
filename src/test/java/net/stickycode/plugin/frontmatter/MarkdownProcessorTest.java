package net.stickycode.plugin.frontmatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class MarkdownProcessorTest {

  @Test
  public void pending() throws IOException {
    check(MarkdownProcessor.Pending, "preamble", MarkdownProcessor.Pending, "");
    check(MarkdownProcessor.Pending, "---", MarkdownProcessor.Frontmatter, "---\n");
  }

  @Test
  public void frontmatter() throws IOException {
    check(MarkdownProcessor.Frontmatter, "preamble", MarkdownProcessor.Frontmatter, "preamble\n");
    check(MarkdownProcessor.Frontmatter, "date: 2020-07-31", MarkdownProcessor.Frontmatter, "date: 2020-07-31\n");
    check(MarkdownProcessor.Frontmatter, "---", MarkdownProcessor.Body, "---\n");
  }

  private void check(MarkdownProcessor start, String in, MarkdownProcessor end, String expected) throws IOException {
    StringWriter out = new StringWriter();
    try (BufferedWriter writer = new BufferedWriter(out);) {
      assertThat(start.consume(in, writer, new FrontmatterRules())).isEqualTo(end);
      writer.flush();
      assertThat(out.getBuffer().toString()).isEqualTo(expected);
    }
  }
}
