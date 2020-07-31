package net.stickycode.plugin.frontmatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FrontmatterUpdate {

  private Path outputDirectory;

  private Path file;

  private MarkdownProcessor processor = MarkdownProcessor.Pending;

  public FrontmatterUpdate(Path file, Path outputDirectory) {
    this.file = file;
    this.outputDirectory = outputDirectory;
  }

  public void process(FrontmatterRules rules) {
    try (BufferedReader reader = Files.newBufferedReader(file);) {
      try (BufferedWriter writer = Files.newBufferedWriter(outputDirectory.resolve(file.getFileName()));) {
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
          processor = processor.consume(line, writer, rules);
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
