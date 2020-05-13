package configuration;

import application.Algorithm;

public enum Configuration {
    instance;

    // common
    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");

    // database
    public final String dataDirectory = userDirectory + fileSeparator + "hsqldb" + fileSeparator;
    public final String databaseFile = dataDirectory + "datastore.db";
    public final String driverName = "jdbc:hsqldb:";
    public final String username = "sa";
    public final String password = "";

    // component
    public String componentsDirectory = userDirectory + fileSeparator + "components" + fileSeparator;
    public String componentDirectory = userDirectory + fileSeparator + "component";
    public Algorithm algorithm = Algorithm.SHIFT;

    public String getCryptoComponentPath() {
        String algo = "";
        switch (algorithm) {
            case SHIFT:
                algo = "shift";
                break;
            case RSA:
                algo = "rsa";
                break;
        }

        return componentsDirectory + "component_" + algo + fileSeparator + "jar" + fileSeparator + algo + ".jar";
    }
    public String getCrackerComponentPath() {
        String algo = "";
        switch (algorithm) {
            case SHIFT:
                algo = "shift";
                break;
            case RSA:
                algo = "rsa";
                break;
        }

        return componentsDirectory + "component_" + algo + "_cracker" + fileSeparator + "jar" + fileSeparator + algo +
                "_cracker.jar";
    }

    // keys
    public String keyfilesDirectory = userDirectory + fileSeparator + "src" + fileSeparator + "keyfiles" + fileSeparator;

    // cracker
    public final int crackTimeout = 30; // in Sekunden
}