package net.stickycode.plugin.frontmatter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class MarkdownVisitor
    extends SimpleFileVisitor<Path> {

  private List<FrontmatterUpdate> files = new ArrayList<>();

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    System.out.println(file);
    if (file.toString().endsWith(".md"))
      files.add(new FrontmatterUpdate(file));

    return FileVisitResult.CONTINUE;
  }

  public List<FrontmatterUpdate> getUpdates() {
    return files;
  }

}
