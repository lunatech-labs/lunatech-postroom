package com.lunatech.postroom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate")
public class GeneratorMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}")
  private MavenProject project;

  @Parameter(defaultValue="${project.build.directory}/generated-sources/postroom", required=true)
  private File targetFolder;

  @Parameter(defaultValue="8")
  private Integer maxArity;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    for(int i = 1; i <= maxArity; i++) {

      Path file = targetFolder.toPath()
          .resolve("com/lunatech/postroom/typed/TypedMappingStage" + i + ".java");
      file.toFile().getParentFile().mkdirs();

      if (!file.toFile().exists()) {
        try (BufferedWriter out = Files.newBufferedWriter(file)) {
          out.write(Generator.generateTypedMappingStage(i));
        } catch (IOException e) {
          throw new MojoExecutionException("Generating TypedMappingStage failed", e);
        }
      }
    }

    for(int i = 1; i <= maxArity; i++) {

      Path file = targetFolder.toPath()
          .resolve("com/lunatech/postroom/typed/TypedListWrapper" + i + ".java");
      file.toFile().getParentFile().mkdirs();

      if (!file.toFile().exists()) {
        try (BufferedWriter out = Files.newBufferedWriter(file)) {
          out.write(Generator.generateTypedListWrapper(i));
        } catch (IOException e) {
          throw new MojoExecutionException("Generating TypedListWrapper failed", e);
        }
      }

    }

    Path mappingsFile = targetFolder.toPath().resolve("com/lunatech/postroom/typed/TypedMappings.java");
    if(!mappingsFile.toFile().exists()) {
      try (BufferedWriter out = Files.newBufferedWriter(mappingsFile)) {
        out.write(Generator.generateFactoryMethods(maxArity));
      } catch (IOException e) {
        throw new MojoExecutionException("Generating Mappings file failed", e);
      }
    }

    project.addCompileSourceRoot(targetFolder.toPath().toString());

  }

}
