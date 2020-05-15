package logger;

import configuration.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class LoggerHelper {
    public static String getContentOfLatestLogfile() {
        File[] files = new File(Configuration.instance.loggerDirectory).listFiles();
        if (files != null) {
            File latestLogfile = null;
            for (File file : files) {
                if (file.isFile()) {
                    if (latestLogfile == null || getUnixSecondsFromFile(file) > getUnixSecondsFromFile(latestLogfile)) {
                        latestLogfile = file;
                    }
                }
            }
            try {
                String path = latestLogfile.getPath();
                byte[] encoded = Files.readAllBytes(Paths.get(path));
                return "newest Logfile: " + path + "\n\n" + new String(encoded, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return "No logfiles present.";
        }
    }

    private static long getUnixSecondsFromFile(File file) {
        String baseName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        String[] parts = baseName.split("_");
        return Long.parseLong(parts[2]);
    }
}
