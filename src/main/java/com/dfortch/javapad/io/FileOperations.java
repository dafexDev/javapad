package com.dfortch.javapad.io;

import java.io.File;
import java.io.IOException;

public interface FileOperations {
    String readFromFile(File file) throws IOException;

    void saveToFile(File file, String content, boolean createFileIfNotExists) throws IOException;
}
