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

  @Test
  public void body() throws IOException {
    check(MarkdownProcessor.Body, "content", MarkdownProcessor.Body, "content\n");
    check(MarkdownProcessor.Body, "---", MarkdownProcessor.Body, "---\n");
  }

  @Test
  public void delete() throws IOException {
    check(MarkdownProcessor.Frontmatter, "date: 2020-07-31", MarkdownProcessor.Frontmatter, "", new DeleteFrontmatterRule("date"));
    check(MarkdownProcessor.Frontmatter, "date: 2020-07-31", MarkdownProcessor.Frontmatter, "date: 2020-07-31\n", new DeleteFrontmatterRule("key"));
  }

  @Test
  public void addHappensAtBeginningOfFrontMatter() throws IOException {
    check(MarkdownProcessor.Pending, "---", MarkdownProcessor.Frontmatter, "---\nkey: value\n", new AddFrontmatterRule().setKey("key").setValue("value"));
  }

  @Test
  public void addDoesntHappenInFrontMatter() throws IOException {
    check(MarkdownProcessor.Frontmatter, "date: 2020-07-31", MarkdownProcessor.Frontmatter, "date: 2020-07-31\n", new AddFrontmatterRule().setKey("key").setValue("value"));
  }

  @Test
  public void addDoesntHappenAtEndOfFrontMatter() throws IOException {
    check(MarkdownProcessor.Frontmatter, "---", MarkdownProcessor.Body, "---\n", new AddFrontmatterRule().setKey("key").setValue("value"));
  }

  private void check(MarkdownProcessor start, String in, MarkdownProcessor end, String expected, FrontmatterRule... rule)
      throws IOException {
    FrontmatterRules rules = new FrontmatterRules();
    rules.add(rule);
    FrontmatterRulesExecution run = new FrontmatterRulesExecution(rules);
    StringWriter out = new StringWriter();
    try (BufferedWriter writer = new BufferedWriter(out);) {
      assertThat(start.consume(in, writer, run)).isEqualTo(end);
      writer.flush();
      assertThat(out.getBuffer().toString()).isEqualTo(expected);
    }
  }
}
