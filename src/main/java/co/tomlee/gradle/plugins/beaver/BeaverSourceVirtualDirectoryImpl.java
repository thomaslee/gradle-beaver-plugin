package co.tomlee.gradle.plugins.beaver;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.util.ConfigureUtil;

public final class BeaverSourceVirtualDirectoryImpl implements BeaverSourceVirtualDirectory {
    private static final String[] filters = { "**/*.beaver", "**/*.grammar", "**/*.g" };

    private final SourceDirectorySet beaver;

    public BeaverSourceVirtualDirectoryImpl(String parentDisplayName, FileResolver fileResolver) {
        final String displayName = String.format("%s Beaver source", parentDisplayName);
        this.beaver = new DefaultSourceDirectorySet(displayName, fileResolver);
        this.beaver.getFilter().include(filters);
    }

    @Override
    public SourceDirectorySet getBeaver() {
        return beaver;
    }

    @Override
    public BeaverSourceVirtualDirectory beaver(Closure closure) {
        ConfigureUtil.configure(closure, getBeaver());
        return this;
    }
}
