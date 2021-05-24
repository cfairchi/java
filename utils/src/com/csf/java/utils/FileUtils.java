package com.csf.java.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static boolean renameFile(final String theOldName, final String theNewName, boolean forceRename) throws IOException {
        boolean success = false;
        File oldFile = new File(theOldName);
        if (!oldFile.exists()) {
            throw new IOException("file not found");
        }
        // File (or directory) with new name
        File newFile = new File(theNewName);
        if (newFile.exists() && forceRename) {
            if (!deleteFile(theNewName)) {
                throw new IOException(theNewName + " already exists and can't be deleted");
            }
        } else {
            if (newFile.exists()) {
                throw new IOException(theNewName + " already exists");
            }
        }
        return oldFile.renameTo(newFile);
    }

    public static boolean deleteFile(final String theFile) {
        boolean success = false;
        try {
            File file = new File(theFile);
            success = file.delete();

        } catch (Exception ex) {
            ex.printStackTrace();
            success = false;

        }
        return success;

    }
}
