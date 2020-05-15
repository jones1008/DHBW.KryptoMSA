package logger;

import configuration.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger implements ILogger{
    private String fileName;

    public Logger(String method, String algo) {
        long unixSeconds = System.currentTimeMillis() / 1000L;
        this.fileName = Configuration.instance.loggerDirectory + method + "_" + algo + "_" + unixSeconds + ".txt";
    }

    public void log(String text) {
        // create file if it doesn't exist yet
        File f = new File(fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write text to file with newline
        try {
            FileWriter fw = new FileWriter(fileName,true);
            fw.write(text+ "\n");
            fw.close();
        } catch(IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
}
