package net.stickycode.plugin.frontmatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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
   * List of files to include. Specified as fileset patterns which are relative to the input directory whose contents
   * is being packaged into the JAR.
   */
  @Parameter
  private String[] includes;

  /**
   * List of files to exclude. Specified as fileset patterns which are relative to the input directory whose contents
   * is being packaged into the JAR.
   */
  @Parameter
  private String[] excludes;

  /**
   * Directory containing the Markdown files to update
   */
  @Parameter(defaultValue = "${project.build.directory}/markdown", required = true)
  private File baseDirectory;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (!baseDirectory.isDirectory())
      throw new MojoFailureException("base directory not found " + baseDirectory.toString());

    if (!baseDirectory.canRead())
      throw new MojoFailureException("base directory cannot be read" + baseDirectory.toString());

    try {
      processFrontmatter(baseDirectory);
    }
    catch (Exception e) {
      throw new MojoFailureException("Failed to process the markdown files in " + baseDirectory.toString(), e);
    }
  }

  List<FrontmatterUpdate> processFrontmatter(File directory) throws IOException {
    MarkdownVisitor visitor = new MarkdownVisitor();
    Files.walkFileTree(directory.toPath(), visitor);
    return visitor.getUpdates();
  }


}
