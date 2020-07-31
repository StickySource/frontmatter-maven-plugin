package net.stickycode.plugin.frontmatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
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

  private List<FrontmatterUpdate> frontMatter(String directory) throws IOException {
    UpdateFrontmatterMojo mojo = new UpdateFrontmatterMojo();
    return mojo.processFrontmatter(new File("src/test/resources",directory));
  }

}
