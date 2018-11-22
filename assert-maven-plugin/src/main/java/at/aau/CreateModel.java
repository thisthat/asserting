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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
     * Output directory path where model files are generated
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "modelDirectory", required = true)
    private File outputDirectory;


    /**
     * Output directory path where model files are generated
     */
    @Parameter(defaultValue = "${basedir}", property = "baseDirectory", required = true)
    private String basedir;

    /**
     * Timeout for processing a file
     */
    @Parameter(defaultValue = "1", property = "nThread", required = true)
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
    private final ExecutorService executor;

    @Inject
    public CreateModel(FileSetManager fileSetManager) {
        this.fileSetManager = fileSetManager;
        this.executor = Executors.newCachedThreadPool();
    }

    public void execute()
        throws MojoExecutionException
    {
        getLog().info("Generating the models");
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
            //Map<Path, Callable<Path>> results = new HashMap<>();
            List<Callable<TaskResult>> tasks = new ArrayList<>();
            for (final String f : includedFiles) {
                final Path path = Paths.get(f);
                getLog().debug("Executing plugin on " + path);
                Callable<TaskResult> c = new CreateModelTask(path);
                tasks.add(c);
            }
            List<Future<TaskResult>> results = null;
            try {
                results = executor.invokeAll(tasks, timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    private void processResult(List<Future<TaskResult>> results) throws InterruptedException {
        if (executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
            getLog().info("Processed " + results.size() + " files");
        } else {
            getLog().error("Timeout processing files");
        }
        long succeed = 0, failed = 0;
        for (Future<TaskResult> entry : results) {
            try {
                TaskResult outputFile = entry.get();
                getLog().debug(outputFile.getInPath() + " => " + outputFile.getOutPath());
                succeed++;
            } catch (ExecutionException | CancellationException e) {
                failed++;
                getLog().error("Error converting ", e);
            }
        }
        long s = results.size();
        double ps = Math.round((100.0 * succeed/s) * 100.0) / 100.0;
        double pf = Math.round((100.0 * failed/s) * 100.0) / 100.0;
        getLog().info(String.format("Converted %d/%d (%.2f%%) files", succeed, s, ps));
        getLog().info(String.format("Failed %d/%d (%.2f%%)files", failed,  s, pf));
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
    class CreateModelTask implements Callable<TaskResult> {

        private final Path input;


        public CreateModelTask(Path input) {
            this.input = input;
        }

        public TaskResult call() throws Exception {
            ProcessFile pf = new ProcessFile(input, basedir, getLog());
            //getLog().debug("FINISH CORRECTLY : " + input);
            return new TaskResult(input, pf.getOutputFiles());
        }
    }

    class TaskResult {
        private Path inPath;
        private String outPath;

        public TaskResult(Path inPath, String outPath) {
            this.inPath = inPath;
            this.outPath = outPath;
        }

        public Path getInPath() {
            return inPath;
        }

        public String getOutPath() {
            return outPath;
        }
    }

}
