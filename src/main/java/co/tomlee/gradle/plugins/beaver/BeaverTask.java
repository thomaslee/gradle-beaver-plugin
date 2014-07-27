package co.tomlee.gradle.plugins.beaver;

import beaver.Parser;
import beaver.comp.ParserGenerator;
import beaver.comp.io.SrcReader;
import beaver.comp.run.Options;
import beaver.comp.util.Log;
import beaver.spec.Grammar;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;

public class BeaverTask extends SourceTask {
    private SourceDirectorySet source;
    private File outputDirectory;
    private FileCollection beaverClasspath;

    @TaskAction
    public void generate() throws IOException, Parser.Exception, Grammar.Exception {
        for (final File file : source.getFiles()) {
            final SrcReader srcReader = new SrcReader(file);
            final Options options = new Options();
            options.dest_dir = getOutputDirectory();
            ParserGenerator.compile(srcReader, options, new Log());
        }
    }

    public void setSource(final SourceDirectorySet source) {
        this.source = source;
    }

    public SourceDirectorySet getSource() {
        return this.source;
    }

    @InputFiles
    public FileCollection getBeaverClasspath() {
        return beaverClasspath;
    }

    public void setBeaverClasspath(final FileCollection beaverClasspath) {
        this.beaverClasspath = beaverClasspath;
    }

    public void setOutputDirectory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @OutputDirectory
    public File getOutputDirectory() {
        return outputDirectory;
    }
}
