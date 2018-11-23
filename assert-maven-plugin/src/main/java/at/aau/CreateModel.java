package at.aau;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import at.aau.model.MethodModel;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Goal which creates the model of the source code
 *
 * @goal generate
 * @phase process-classes
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
@Mojo(name = "create-model", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class CreateModel
    extends AbstractMojo
{

    /**
     * Output directory path where model files are generated
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "modelDirectory", required = true)
    private File outputDirectory;


    /**
     * Output directory path where model files are generated
     */
    @Parameter(defaultValue = "${basedir}", property = "baseDirectory", required = true)
    private String basedir;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter( defaultValue = "${localRepository}", readonly = true, required = true )
    private ArtifactRepository local;

    /**
     * Timeout for processing a file
     */
    @Parameter(defaultValue = "1", property = "nThread", required = true)
    private int nThread = 1;

    /**
     * Timeout for processing a file
     */
    @Parameter(defaultValue = "60", property = "timeout", required = true)
    private long timeout;

    /**
     * A specific <code>fileSet</code> rule to select files and directories.
     * Fileset spec: https://maven.apache.org/shared/file-management/fileset.html
     */
    @Parameter
    private FileSet inputFiles;

    @Parameter
    private String[] filters;


    private Path inputDirPath, outputDirPath;

    /* Injected */
    private final FileSetManager fileSetManager;

    long succeed = 0, failed = 0, skipped = 0;

    @Inject
    public CreateModel(FileSetManager fileSetManager) {
        this.fileSetManager = fileSetManager;
    }

    public void execute() {

        StringBuilder c = new StringBuilder();
        for(Artifact a : this.project.getDependencyArtifacts()){
            if(a != null && a.getFile() != null)
                c.append(":").append(a.getFile().getAbsolutePath());
        }
        getLog().info("Classpath: " + c);
        if (filters == null) {
            setDefaultInput();
        } else {
            setFilters();
        }
        inputDirPath = Paths.get(inputFiles.getDirectory());
        outputDirPath = outputDirectory.toPath();
        String[] includedFiles = fileSetManager.getIncludedFiles(inputFiles);
        if (includedFiles == null || includedFiles.length == 0) {
            getLog().info("SKIP: There are no input files. " + getInputFilesToString());
        } else {
            getLog().debug(Arrays.toString(includedFiles));
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }
            for (final String f : includedFiles) {
                try {
                    final Path input = Paths.get(f);
                    ProcessFile pf = new ProcessFile(input, basedir, getLog(), c.toString());
                    List<MethodModel> models = pf.getOutputFiles();
                    processResults(models);
                } catch (Exception ex) {
                    getLog().error(ex);
                }
            }
        }
        long s = includedFiles == null ? 0 : includedFiles.length;
        double ps = Math.round((100.0 * succeed/s) * 100.0) / 100.0;
        double pf = Math.round((100.0 * failed/s) * 100.0) / 100.0;
        double pk = Math.round((100.0 * (s - (failed + succeed))/s) * 100.0) / 100.0;
        getLog().info(String.format("Converted %d/%d (%.2f%%) files", succeed, s, ps));
        getLog().info(String.format("Failed %d/%d (%.2f%%) files", failed,  s, pf));
        getLog().info(String.format("Skipped %d/%d (%.2f%%) files", s - (failed + succeed),  s, pk));
    }

    private void processResults(List<MethodModel> results) {
        //save file
        boolean failure = false;
        for(MethodModel model : results) {
            getLog().info(String.format("Saving: %s in %s", model.getModelName(), outputDirectory));
            try (PrintWriter out = new PrintWriter(outputDirectory + "/" + model.getModelName())) {
                out.println(model.getModel());
            } catch (FileNotFoundException e) {
                getLog().error(String.format("Cannot save model %s", model.getModelName()), e);
                failure = true;
            }
        }
        if(failure)
            failed++;
        else
            succeed++;
    }

    private void setDefaultInput() {
        this.inputFiles = new FileSet();
        this.inputFiles.addInclude("**/*.java");
        this.inputFiles.setDirectory(".");
        getLog().info("'inputFiles' is not configured, using defaults: " + getInputFilesToString());
    }
    private void setFilters() {
        this.inputFiles = new FileSet();
        for(String filter : filters) {
            this.inputFiles.addInclude(filter);
        }
        this.inputFiles.setDirectory(this.basedir);
        getLog().info("'inputFiles' is configured using: " + getInputFilesToString());
    }

    private String getInputFilesToString() {
        return "Fileset matching " + inputFiles.getIncludes() + " in " + inputFiles.getDirectory();
    }
}
