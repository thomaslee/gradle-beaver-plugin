package co.tomlee.gradle.plugins.beaver;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.Callable;

public final class BeaverPlugin implements Plugin<Project> {
    private final FileResolver fileResolver;

    @Inject
    public BeaverPlugin(final FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(JavaPlugin.class);

        configureConfigurations(project);
        configureSourceSets(project);
    }

    private void configureConfigurations(final Project project) {
        Configuration beaverConfiguration = project.getConfigurations().create("beaver").setVisible(false);
        project.getConfigurations().getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(beaverConfiguration);
    }

    private void configureSourceSets(final Project project) {
        project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().all(new Action<SourceSet>() {
            @Override
            public void execute(SourceSet sourceSet) {
                //
                // This logic borrowed from the antlr plugin.
                // 1. Add a new 'beaver' virtual directory mapping
                //
                final BeaverSourceVirtualDirectoryImpl beaverSourceSet =
                    new BeaverSourceVirtualDirectoryImpl(((DefaultSourceSet) sourceSet).getDisplayName(), fileResolver);
                new DslObject(sourceSet).getConvention().getPlugins().put("beaver", beaverSourceSet);
                final String srcDir = String.format("src/%s/beaver", sourceSet.getName());
                beaverSourceSet.getBeaver().srcDir(srcDir);
                sourceSet.getAllSource().source(beaverSourceSet.getBeaver());

                //
                // 2. Create a BeaverTask for this sourceSet
                //
                final String taskName = sourceSet.getTaskName("generate", "BeaverSource");
                final BeaverTask beaverTask = project.getTasks().create(taskName, BeaverTask.class);
                beaverTask.setDescription(String.format("Processes the %s Beaver grammars.", sourceSet.getName()));

                //
                // 3. Set up convention mapping for default sources (allows user to not have to specify)
                //
                beaverTask.setSource(beaverSourceSet.getBeaver());

                //
                // 4. Set up convention mapping for handling the 'beaver' dependency configuration
                //
                beaverTask.getConventionMapping().map("beaverClasspath", new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return project.getConfigurations().getByName("beaver").copy().setTransitive(true);
                    }
                });

                //
                // 5. Set up the beaver output directory (adding to javac inputs)
                //
                final String outputDirectoryName =
                    String.format("%s/generated-src/beaver/%s", project.getBuildDir(), sourceSet.getName());
                final File outputDirectory = new File(outputDirectoryName);
                beaverTask.setOutputDirectory(outputDirectory);
                sourceSet.getJava().srcDir(outputDirectory);

                //
                // 6. Register the fact that beaver should run before compiling.
                //
                project.getTasks().getByName(sourceSet.getCompileJavaTaskName()).dependsOn(taskName);
            }
        });
    }
}
