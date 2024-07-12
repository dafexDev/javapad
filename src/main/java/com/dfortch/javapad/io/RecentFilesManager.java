package com.dfortch.javapad.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface RecentFilesManager {
    void addRecentFile(File file) throws IOException;

    List<File> getRecentFiles() throws IOException;

    void removeRecentFile(File file) throws IOException;

    void clearRecentFiles() throws IOException;
}
