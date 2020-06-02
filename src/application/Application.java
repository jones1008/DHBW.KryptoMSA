package application;

import cryptoManager.CryptoManager;
import persistence.*;

import java.time.Instant;

public class Application {
    public static void main(String... args)
    {
//        cryptoManagerDemo();
//        System.out.println();
//        crackerDemo();
        rsaCrackerDemo();
    }

    private static void hsqldbDemo() {
        System.out.println("-----hsqldb Demo-----");
        DB.instance.setupConnection();

        DBParticipant.instance.dropTableParticipants();
        DBType.instance.dropTableTypes();

        DBType.instance.createTableTypes();
        DBAlgorithm.instance.createTableAlgorithms();
        DBParticipant.instance.createTableParticipants();
        DBChannel.instance.createTableChannel();
        DBMessage.instance.createTableMessages();

        DBType.instance.insertDataTableTypes("normal");
        DBType.instance.insertDataTableTypes("intruder");

        DBAlgorithm.instance.insertDataTableAlgorithms("rsa");
        DBAlgorithm.instance.insertDataTableAlgorithms("shift");

        DB.instance.shutdown();
    }

    private static void cryptoManagerDemo() {
        System.out.println("-----Crypto Manager Demo-----");
        CryptoManager manager = new CryptoManager();
        System.out.println("Algorithms: ");
        for (String algo: manager.showAlgorithms())
        {
            System.out.println(algo);
        }

        System.out.println();
        System.out.println("RSA:");
        String message = "test";
        String algorithm = "rsa";
        String keyfile = "rsa_keyfile.json";
        String encrypted = manager.encrypt(message, algorithm, keyfile);
        String decrypted = manager.decrypt(encrypted, algorithm, keyfile);
        System.out.println("message: " + message);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);

        System.out.println();
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
        System.out.println("-----Cracker Demo-----");
        CryptoManager manager = new CryptoManager();
        System.out.println("Crack Shift: ");
        String message = "prvedfk"; // MOSBACH
        message = "Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. " +
                "Iznx fzyjr ajq jzr nwnzwj itqtw ns mjsiwjwny ns azquzyfyj ajqny jxxj rtqjxynj htsxjvzfy, ajq nqqzr itqtwj jz kjzlnfy szqqf kfhnqnxnx fy ajwt jwtx jy fhhzrxfs jy nzxyt tint inlsnxxnr vzn gqfsiny uwfjxjsy qzuyfyzr eewnq ijqjsny fzlzj iznx itqtwj yj kjzlfny szqqf kfhnqnxn. Qtwjr nuxzr itqtw xny frjy, htsxjhyjyzjw finunxhnsl jqny, xji infr stszrrd sngm jznxrti ynshnizsy zy qftwjjy itqtwj rflsf fqnvzfr jwfy atqzyufy.";
        String algorithm = "shift";
        String cracked = manager.crack(message, algorithm);
        System.out.println("message: " + message);
        System.out.println("cracked: ");
        System.out.println(cracked);

        System.out.println();
        System.out.println("Crack RSA: ");
        message = "MDFsO4jlFDkcLgM1";
        algorithm = "rsa";
        cracked = manager.crack(message, algorithm);
        System.out.println("message: " + message);
        System.out.println("cracked: ");
        System.out.println(cracked);
    }

    private static void rsaCrackerDemo() {
        CryptoManager manager = new CryptoManager();
        System.out.println("started at " + Instant.now());
        System.out.println("encrypt RSA: ");
        String message = "morpheus";
        String algorithm = "rsa";
        String keyfile = "rsa_keyfile.json";
        String encrypted = manager.encrypt(message, algorithm, keyfile);
        String decrypted = manager.decrypt(encrypted, algorithm, keyfile);
        System.out.println("plain message: " + message + "; encrypted: " + encrypted + "; used keyfile: " + keyfile + "; decrypted: " + decrypted);

        System.out.println("Crack RSA: ");
        String cracked = manager.crack(encrypted, algorithm);
        System.out.println("message: " + message);
        System.out.println("cracked: ");
        System.out.println(cracked);
    }
}