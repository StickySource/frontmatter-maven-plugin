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

  /**
   * The rules to filter/transform/edit the frontmatter
   */
  @Parameter(required = false)
  private FrontmatterRule[] rules;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!sourceDirectory.isDirectory())
      throw new MojoFailureException("base directory not found " + sourceDirectory.toString());

    if (!sourceDirectory.canRead())
      throw new MojoFailureException("base directory cannot be read" + sourceDirectory.toString());

    try {
      FrontmatterRules plan = new FrontmatterRules();
      if (rules != null)
        plan.add(rules);

      List<FrontmatterUpdate> updates = processFrontmatter(sourceDirectory.toPath(), outputDirectory.toPath(), plan);
      getLog().info("processed " + updates.size() + " file(s)");
    }
    catch (Exception e) {
      throw new MojoFailureException("Failed to process the markdown files in " + sourceDirectory.toString(), e);
    }
  }

  List<FrontmatterUpdate> processFrontmatter(Path source, Path output, FrontmatterRules rules) throws IOException {
    if (source.equals(output))
      getLog().info("Apply " + rules + " to markdown in " + source);
    else
      getLog().info("Apply " + rules + " when copying from " + source + " to " + output);

    Files.createDirectories(output);

    return Files.walk(source)
      .filter(Files::isReadable)
      .filter(Files::isRegularFile)
      .filter(UpdateFrontmatterMojo::isMarkdown)
      .map(f -> {
        getLog().debug("processing " + f.toString());
        return f;
      })
      .map(m -> new FrontmatterUpdate(m, output.resolve(source.relativize(m))))
      .peek(u -> u.process(new FrontmatterRulesExecution(rules)))
      .collect(Collectors.toList());

  }

  public static boolean isMarkdown(Path path) {
    return String.valueOf(path).endsWith(".md");
  }

}
