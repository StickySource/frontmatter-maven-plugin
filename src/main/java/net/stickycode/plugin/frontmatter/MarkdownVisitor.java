package net.stickycode.plugin.frontmatter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MarkdownVisitor
    implements Consumer<Path> {

  private List<FrontmatterUpdate> files = new ArrayList<>();

  public List<FrontmatterUpdate> getUpdates() {
    return files;
  }

  @Override
  public void accept(Path file) {
//    System.out.println(file);
//    if (file.toString().endsWith(".md"))
//      files.add(new FrontmatterUpdate(file));
  }

}
