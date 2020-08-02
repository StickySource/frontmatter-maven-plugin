package net.stickycode.plugin.frontmatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class FrontmatterRulesTest {

  @Test
  public void deleteBlockesMatchingFrontmatter() {
    FrontmatterRules rules = new FrontmatterRules();
    rules.add(new DeleteFrontmatterRule("date"));
    assertThat(rules.allow("field: asdfalkjaf")).isTrue();
    assertThat(rules.allow("date:")).isTrue();
    assertThat(rules.allow("date: 2020-07-10")).isFalse();
  }

  @Test
  public void addAlwaysAllows() {
    FrontmatterRules rules = new FrontmatterRules();
    rules.add(new AddFrontmatterRule().setKey("date").setValue("2020-07-03"));
    assertThat(rules.allow("field: asdfalkjaf")).isTrue();
    assertThat(rules.allow("date:")).isTrue();
    assertThat(rules.allow("date: 2020-07-10")).isTrue();
  }

  @Test
  public void addition() throws IOException {
    check("date: 2020-07-03\n", new AddFrontmatterRule().setKey("date").setValue("2020-07-03"));
  }

  @Test
  public void additions() throws IOException {
    check("date: 2020-07-03\ntag: one,two,three\n",
      new AddFrontmatterRule().setKey("date").setValue("2020-07-03"),
      new AddFrontmatterRule().setKey("tag").setValue("one,two,three")
      );
  }

  private void check(String expected, FrontmatterRule... rule) throws IOException {
    FrontmatterRules rules = new FrontmatterRules();
    rules.add(rule);
    StringWriter out = new StringWriter();
    try (BufferedWriter writer = new BufferedWriter(out);) {
      rules.applyAdditions(writer);
      writer.flush();
      assertThat(out.getBuffer().toString()).isEqualTo(expected);
    }
  }

}
