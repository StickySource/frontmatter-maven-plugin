package net.stickycode.plugin.frontmatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FrontmatterUpdate {

  private Path target;

  private Path source;

  private Path processed;

  private MarkdownProcessor processor = MarkdownProcessor.Pending;

  public FrontmatterUpdate(Path s, Path t) {
    this.source = s;
    this.target = t;
    if (s.equals(t)) // if the processing is in-place use a temporary intermediary
      this.processed = processedPath();
    else
      this.processed = t;
  }

  private Path processedPath() {
    try {
      return Files.createTempFile("fmu-", ".md");
    }
    catch (IOException e) {
      throw new RuntimeException("Failed to create a temp file", e);
    }
  }

  public void process(FrontmatterRulesExecution rules) {
    try {
      Files.createDirectories(target.getParent()); // nested files need parents
      try (BufferedReader reader = Files.newBufferedReader(source);) {
        try (BufferedWriter writer = Files.newBufferedWriter(processed);) {
          for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            processor = processor.consume(line, writer, rules);
          }
        }
      }

      if (!processed.equals(target)) // if the files are being updated in-place overwrite with the processed version
        Files.move(processed, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
