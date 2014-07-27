package co.tomlee.gradle.plugins.beaver;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;

public interface BeaverSourceVirtualDirectory {
    SourceDirectorySet getBeaver();
    BeaverSourceVirtualDirectory beaver(Closure closure);
}
