package helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFinder {
    public static List<String> findPngFilePaths(String folderPath) {
        List<String> pngFilePaths = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.isDirectory()) {
            System.out.println("Invalid folder path: " + folderPath);
            return pngFilePaths;
        }
        searchPngFiles(folder, pngFilePaths);
        return pngFilePaths;
    }

    private static void searchPngFiles(File directory, List<String> pngFilePaths) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchPngFiles(file, pngFilePaths);
                } else if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                    pngFilePaths.add(file.getAbsolutePath());
                }
            }
        }
    }

}
