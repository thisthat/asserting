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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Goal which creates the model of the source code
 *
 * @phase test
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
@Mojo(name = "extract-cp", defaultPhase = LifecyclePhase.COMPILE)
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
     * Boolean if add recovery or not
     */
    @Parameter(defaultValue = "true", property = "recovery", required = true)
    private boolean recovery;

    /**
     * Boolean if model time properties or not
     */
    @Parameter(defaultValue = "true", property = "timeEnabled", required = true)
    private boolean timeEnabled;

    /**
     * Boolean if model mathematical expressions or not
     */
    @Parameter(defaultValue = "true", property = "mathEnabled", required = true)
    private boolean mathEnabled;

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

        String prj = this.project.getModel().getArtifactId();

        StringBuilder c = new StringBuilder();
        try {
            List<String> elements = this.project.getTestClasspathElements();
            for(String t : elements) {
                c.append(":").append(t);
            }
        } catch (Exception e){ }
        for(Artifact a : this.project.getDependencyArtifacts()){
            if(a != null && a.getFile() != null)
                c.append(":").append(a.getFile().getAbsolutePath());
        }
        getLog().info("===================================");
        getLog().info(prj);
        getLog().info("Classpath: " + c);
//        try {
//            getLog().info("Classpath: " + Arrays.toString(this.project.getTestClasspathElements().toArray()));
//        } catch (Exception e){
//            getLog().info("EXCEPTION");
//            getLog().info(e.getMessage());
//        }
        getLog().info("===================================");

        String fileName = prj + ".classpath.txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(c);
            printWriter.close();
        } catch (Exception _) { }


    }


}
