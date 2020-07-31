package net.stickycode.plugin.frontmatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(threadSafe = true, name = "update")
public class UpdateFrontmatterMojo
    extends AbstractMojo {

  /**
   * The line separator used when rewriting the pom, this to defaults to your platform encoding but if you fix your encoding despite
   * platform then you should use that.
   */
  @Parameter
  private LineSeparator lineSeparator = LineSeparator.defaultValue();

  /**
   * Directory containing the Markdown files to update
   */
  @Parameter(defaultValue = "src/main/markdown", required = true)
  private File sourceDirectory;

  /**
   * Where to write the updated Markdown files
   */
  @Parameter(defaultValue = "${project.build.directory}/markdown", required = false)
  private File outputDirectory;

  @Parameter(required = true)
  private FrontmatterRules rules;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!sourceDirectory.isDirectory())
      throw new MojoFailureException("base directory not found " + sourceDirectory.toString());

    if (!sourceDirectory.canRead())
      throw new MojoFailureException("base directory cannot be read" + sourceDirectory.toString());

    try {
      processFrontmatter(sourceDirectory.toPath(), outputDirectory.toPath(), rules);
    }
    catch (Exception e) {
      throw new MojoFailureException("Failed to process the markdown files in " + sourceDirectory.toString(), e);
    }
  }

  List<FrontmatterUpdate> processFrontmatter(Path source, Path output, FrontmatterRules rules) throws IOException {
    Files.createDirectories(output);

    return Files.walk(source)
      .filter(Files::isReadable)
      .filter(Files::isRegularFile)
      .filter(UpdateFrontmatterMojo::isMarkdown)
      .map(m -> new FrontmatterUpdate(m, output))
      .peek(u -> u.process(rules))
      .collect(Collectors.toList());

  }

  public static boolean isMarkdown(Path path) {
    return String.valueOf(path).endsWith(".md");
  }

}
