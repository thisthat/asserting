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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.model.fileset.FileSet;
import org.apache.maven.shared.model.fileset.util.FileSetManager;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Goal which creates the model of the source code
 *
 * @goal generate-model
 * 
 * @phase generate_resources
 */
@Mojo(name = "create-model", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class CreateModel
    extends AbstractMojo
{

    /**
     * Output directory path where HTML files are generated
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "modelDirectory", required = true)
    private File outputDirectory;

    /**
     * Timeout for processing a file
     */
    @Parameter(defaultValue = "2", property = "nThread", required = true)
    private int nThread;

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


    private Path inputDirPath, outputDirPath;

    /* Injected */
    private final FileSetManager fileSetManager;
    private final ScheduledExecutorService executor;

    @Inject
    public CreateModel(FileSetManager fileSetManager) {
        this.fileSetManager = fileSetManager;
        this.executor = Executors.newScheduledThreadPool(nThread);
    }

    public void execute()
        throws MojoExecutionException
    {
        getLog().info("Generating the model");
        if (inputFiles == null) {
            setDefaultInput();
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
            Map<Path, Future<Path>> results = new HashMap<>();
            for (String f : includedFiles) {
                final Path path = Paths.get(f);
                getLog().debug("Executing plugin on " + path);
                results.put(path, executor.submit(new CreateModelTask(path)));
            }
            try {
                getLog().debug("Processing results");
                executor.shutdown();
                processResult(results);
            } catch (InterruptedException e) {
                throw new MojoExecutionException(e.getLocalizedMessage(), e);
            }
        }
    }

    private void processResult(Map<Path, Future<Path>> results) throws InterruptedException {
        if (executor.awaitTermination(timeout, TimeUnit.NANOSECONDS)) {
            getLog().info("Processed " + results.size() + " files");
        } else {
            getLog().error("Timeout processing files");
        }
        for (Map.Entry<Path, Future<Path>> entry : results.entrySet()) {
            try {
                Path outputFile = entry.getValue().get();
                getLog().debug(entry.getKey() + " > " + outputFile);
            } catch (ExecutionException e) {
                getLog().error("Error converting " + entry.getKey(), e);
            }
        }
    }

    private void setDefaultInput() {
        this.inputFiles = new FileSet();
        this.inputFiles.addInclude("**/*.java");
        this.inputFiles.setDirectory(".");
        getLog().info("'inputFiles' is not configured, using defaults: " + getInputFilesToString());
    }
    private String getInputFilesToString() {
        return "Fileset matching " + inputFiles.getIncludes() + " in " + inputFiles.getDirectory();
    }

    static int i = 0;
    class CreateModelTask implements Callable<Path> {

        private final Path input;


        public CreateModelTask(Path input) {
            this.input = input;
        }

        public Path call() throws Exception {
            Path out = inputDirPath;
            //if(i++ > 1) {
                Thread.sleep(1000);
            //}
            /**
             * HERE IT COMES THE MAGIC
             */
//            getLog().info("Processing " + input + " > " + out);
//            String html = javaToSmt.getHtml(input);
//            Files.write(out, html.getBytes(), StandardOpenOption.CREATE);
            return out;
        }


    }

}
