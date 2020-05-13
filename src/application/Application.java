package application;

import cryptoManager.CryptoManager;
import persistence.HSQLDB;

public class Application {
    public static void main(String... args)
    {
        cryptoManagerDemo();
//        crackerDemo();
    }

    private static void hsqldbDemo() {
        // hsqldb demo
        HSQLDB.instance.setupConnection();

        HSQLDB.instance.dropTableParticipants();
        HSQLDB.instance.dropTableTypes();

        HSQLDB.instance.createTableTypes();
        HSQLDB.instance.createTableAlgorithms();
        HSQLDB.instance.createTableParticipants();
        HSQLDB.instance.createTableChannel();
        HSQLDB.instance.createTableMessages();

        HSQLDB.instance.insertDataTableTypes("normal");
        HSQLDB.instance.insertDataTableTypes("intruder");

        HSQLDB.instance.insertDataTableAlgorithms("rsa");
        HSQLDB.instance.insertDataTableAlgorithms("shift");

        HSQLDB.instance.insertDataTableParticipants("branch_hkg", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_cpt", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_sfo", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_syd", 1);
        HSQLDB.instance.insertDataTableParticipants("branch_wuh", 1);
        HSQLDB.instance.insertDataTableParticipants("msa", 2);

        HSQLDB.instance.shutdown();
    }

    private static void cryptoManagerDemo() {
        CryptoManager manager = new CryptoManager();
        System.out.println("RSA:");
        String message = "dhbw";
        String algorithm = "rsa";
        String keyfile = "rsa_keyfile.json";
        String encrypted = manager.encrypt(message, algorithm, keyfile);
        String decrypted = manager.decrypt(encrypted, algorithm, keyfile);
        System.out.println("message: " + message);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);

        System.out.println("SHIFT:");
        algorithm = "shift";
        keyfile = "shift_keyfile.json";
        encrypted = manager.encrypt(message, algorithm, keyfile);
        decrypted = manager.decrypt(encrypted, algorithm, keyfile);
        System.out.println("message: " + message);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);
    }

    private static void crackerDemo() {
        CryptoManager manager = new CryptoManager();
        System.out.println("Crack Shift: ");
        String message = "prvedfk";
        String algorithm = "shift";
        String cracked = manager.crack(message, algorithm);
        System.out.println("message: " + message);
        System.out.println("cracked: ");
        System.out.println(cracked);
    }
}