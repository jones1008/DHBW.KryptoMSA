package configuration;

import application.Algorithm;

public enum Configuration {
    instance;

    // common
    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;

    // database
    public final String databaseDirectory = dataDirectory + "hsqldb" + fileSeparator;
    public final String databaseFile = databaseDirectory + "datastore.db";
    public final String driverName = "jdbc:hsqldb:";
    public final String username = "sa";
    public final String password = "";

    // component
    public String componentsDirectory = userDirectory + fileSeparator + "components" + fileSeparator;
    public Algorithm algorithm = Algorithm.SHIFT;

    public String getComponentPath(boolean cracker) {
        String crackerExtension = cracker ? "_cracker" : "";
        String algo = "";
        switch (algorithm) {
            case SHIFT:
                algo = "shift";
                break;
            case RSA:
                algo = "rsa";
                break;
        }

        return componentsDirectory + "component_" + algo + crackerExtension
                + fileSeparator + "jar" + fileSeparator + algo + crackerExtension
                + ".jar";
    }

    // keys
    public String keyfilesDirectory = dataDirectory + "keyfiles" + fileSeparator;

    // cracker
    public int crackTimeout = 30; // in Sekunden

    // logger
    public String loggerDirectory = dataDirectory + "logfiles" + fileSeparator;
}