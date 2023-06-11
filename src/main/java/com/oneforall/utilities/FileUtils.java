package com.oneforall.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static File getFile(String fileName) throws IOException {
        if (FileUtils.class.getClassLoader().getResourceAsStream(fileName) != null) {
            InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
            File file = new File(fileName, "");
            org.apache.commons.io.FileUtils.copyInputStreamToFile(resourceAsStream, file);
            return file;
        } else {
            return new File(fileName);
        }
    }


}
