package net.stickycode.plugin.frontmatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UpdateFrontmatterMojoTest {

  @Test
  public void empty() throws IOException {
    assertThat(frontMatter("empty")).hasSize(0);
  }

  @Test
  public void one() throws IOException {
    assertThat(frontMatter("one")).hasSize(1);
  }

  @Test
  public void many() throws IOException {
    assertThat(frontMatter("many")).hasSize(4);
  }

  @Test
  public void includes() throws IOException {
    assertThat(frontMatter("examples")).hasSize(2);
  }

  @Test
  public void delete() throws IOException {
    assertThat(frontMatter("examples", new DeleteFrontmatterRule("date"))).hasSize(2);
    check("date.md", "---", "---", "", "some content here", "EOF");
  }

  @Test
  public void nodelete() throws IOException {
    assertThat(frontMatter("examples", new DeleteFrontmatterRule("key"))).hasSize(2);
    check("date.md", "---", "date: 2020-07-31", "---", "", "some content here", "EOF");
  }

  @Test
  public void add() throws IOException {
    assertThat(frontMatter("examples", new AddFrontmatterRule().setKey("key").setValue("value"))).hasSize(2);
    check("date.md", "---", "date: 2020-07-31", "key: value", "---", "", "some content here", "EOF");
  }

  @Test
  public void twoAddsDoesJustOne() throws IOException {
    assertThat(frontMatter("examples", new AddFrontmatterRule().setKey("key").setValue("other"),
      new AddFrontmatterRule().setKey("key").setValue("value"))).hasSize(2);
    check("date.md", "---", "date: 2020-07-31", "key: other", "---", "", "some content here", "EOF");
  }

  @Test
  public void noadd() throws IOException {
    assertThat(frontMatter("examples", new AddFrontmatterRule().setKey("date").setValue("other"))).hasSize(2);
    check("date.md", "---", "date: 2020-07-31", "---", "", "some content here", "EOF");
  }

  @Test
  public void inplace() throws IOException {
    assertThat(frontMatter("examples")).hasSize(2);
    check("date.md", "---", "date: 2020-07-31", "---", "", "some content here", "EOF");
    assertThat(inplace(new AddFrontmatterRule().setKey("key").setValue("value"))).hasSize(2);
    check("date.md", "---", "date: 2020-07-31", "key: value", "---", "", "some content here", "EOF");
    assertThat(inplace(new AddFrontmatterRule().setKey("two").setValue("three"), new DeleteFrontmatterRule("date"))).hasSize(2);
    check("date.md", "---", "key: value", "two: three", "---", "", "some content here", "EOF");
  }

  private void check(String resultName, String... expectation) throws IOException {
    assertThat(Files.readAllLines(resultPath(resultName))).containsExactly(expectation);
  }

  private Path resultPath(String resultName) {
    return Paths.get(
      "target",
      Thread.currentThread().getStackTrace()[3].getMethodName(),
      resultName);
  }

  private List<FrontmatterUpdate> frontMatter(String directory, FrontmatterRule... frontmatterRules) throws IOException {
    UpdateFrontmatterMojo mojo = new UpdateFrontmatterMojo();
    FrontmatterRules rules = new FrontmatterRules();
    rules.add(frontmatterRules);
    return mojo.processFrontmatter(
      Paths.get("src/test/resources", directory),
      Paths.get("target", Thread.currentThread().getStackTrace()[2].getMethodName()),
      rules);
  }

  private List<FrontmatterUpdate> inplace(FrontmatterRule... frontmatterRules) throws IOException {
    UpdateFrontmatterMojo mojo = new UpdateFrontmatterMojo();
    FrontmatterRules rules = new FrontmatterRules();
    rules.add(frontmatterRules);
    return mojo.processFrontmatter(
      Paths.get("target", Thread.currentThread().getStackTrace()[2].getMethodName()),
      Paths.get("target", Thread.currentThread().getStackTrace()[2].getMethodName()),
      rules);
  }

}
