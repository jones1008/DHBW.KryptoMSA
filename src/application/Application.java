package application;

import companyNetwork.Channel;
import companyNetwork.Participant;
import companyNetwork.ParticipantType;
import cryptoManager.CryptoManager;
import persistence.HSQLDB;

public class Application {
    public static void main(String... args)
    {
        fillDBWithChannels();
//        cryptoManagerDemo();
//        System.out.println();
//        crackerDemo();
    }

    private static void hsqldbDemo() {
        // hsqldb demo
        System.out.println("-----hsqldb Demo-----");
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

//        HSQLDB.instance.insertDataTableParticipants("branch_hkg", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_cpt", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_sfo", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_syd", 1);
//        HSQLDB.instance.insertDataTableParticipants("branch_wuh", 1);
//        HSQLDB.instance.insertDataTableParticipants("msa", 2);

        HSQLDB.instance.shutdown();
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
        String message = "morpheus";
        String algorithm = "rsa";
        String keyfile = "rsa_keyfile2.json";
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
                "Iznx fzyjr ajq jzr nwnzwj itqtw ns mjsiwjwny ns azquzyfyj ajqny jxxj rtqjxynj htsxjvzfy, ajq nqqzr itqtwj jz kjzlnfy szqqf kfhnqnxnx fy ajwt jwtx jy fhhzrxfs jy nzxyt tint inlsnxxnr vzn gqfsiny uwfjxjsy qzuyfyzr eewnq ijqjsny fzlzj iznx itqtwj yj kjzlfny szqqf kfhnqnxn. Qtwjr nuxzr itqtw xny frjy, htsxjhyjyzjw finunxhnsl jqny, xji infr stszrrd sngm jznxrti ynshnizsy zy qftwjjy itqtwj rflsf fqnvzfr jwfy atqzyufy. " +
                "Zy bnxn jsnr fi rnsnr ajsnfr, vznx stxywzi jcjwhn yfynts zqqfrhtwujw xzxhnuny qtgtwynx snxq zy fqnvznu jc jf htrrtit htsxjvzfy. Iznx fzyjr ajq jzr nwnzwj itqtw ns mjsiwjwny ns azquzyfyj ajqny jxxj rtqjxynj htsxjvzfy, ajq nqqzr itqtwj jz kjzlnfy szqqf kfhnqnxnx fy ajwt jwtx jy fhhzrxfs jy nzxyt tint inlsnxxnr vzn gqfsiny uwfjxjsy qzuyfyzr eewnq ijqjsny fzlzj iznx itqtwj yj kjzlfny szqqf kfhnqnxn. " +
                "Sfr qngjw yjrutw hzr xtqzyf stgnx jqjnkjsi tuynts htslzj snmnq nrujwinjy itrnsl ni vzti rfenr uqfhjwfy kfhjw utxxnr fxxzr. Qtwjr nuxzr itqtw xny frjy, htsxjhyjyzjw finunxhnsl jqny, xji infr stszrrd sngm jznxrti ynshnizsy zy qftwjjy itqtwj rflsf fqnvzfr jwfy atqzyufy. Zy bnxn jsnr fi rnsnr ajsnfr, vznx stxywzi jcjwhn yfynts zqqfrhtwujw xzxhnuny qtgtwynx snxq zy fqnvznu jc jf htrrtit htsxjvzfy. " +
                "Iznx fzyjr ajq jzr nwnzwj itqtw ns mjsiwjwny ns azquzyfyj ajqny jxxj rtqjxynj htsxjvzfy, ajq nqqzr itqtwj jz kjzlnfy szqqf kfhnqnxnx. " +
                "Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, Fy fhhzxfr fqnvzdfr infr infr itqtwj itqtwjx izt jnwrti jtx jwfy, jy stszrd xji yjrutw jy jy nsanizsy ozxyt qfgtwj Xyjy hqnyf jf jy lzgjwlwjs, pfxi rflsf st wjgzr. xfshyzx xjf xji yfpnrfyf zy ajwt atqzuyzf. jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy. " +
                "Htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx. " +
                "Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. Qtwjr nuxzr itqtw xny frjy, htsxjyjyzw xfinuxhnsl jqnyw, xji infr stszrd jnwrti yjrutw nsanizsy zy qfgtwj jy itqtwj rflsf fqnvzdfr jwfy, xji infr atqzuyzf. Fy ajwt jtx jy fhhzxfr jy ozxyt izt itqtwjx jy jf wjgzr. Xyjy hqnyf pfxi lzgjwlwjs, st xjf yfpnrfyf xfshyzx jxy Qtwjr nuxzr itqtw xny frjy. " +
                "Iznx fzyjr ajq jzr nwnzwj itqtw ns mjsiwjwny ns azquzyfyj ajqny jxxj rtqjxynj htsxjvzfy, ajq nqqzr itqtwj jz kjzlnfy szqqf kfhnqnxnx fy ajwt jwtx jy fhhzrxfs jy nzxyt tint inlsnxxnr vzn gqfsiny uwfjxjsy qzuyfyzr eewnq ijqjsny fzlzj iznx itqtwj yj kjzlfny szqqf kfhnqnxn. Qtwjr nuxzr itqtw xny frjy, htsxjhyjyzjw finunxhnsl jqny, xji infr stszrrd sngm jznxrti ynshnizsy zy qftwjjy itqtwj rflsf fqnvzfr jwfy atqzyufy. " +
                "Zy bnxn jsnr fi rnsnr ajsnfr, vznx stxywzi jcjwhn yfynts zqqfrhtwujw xzxhnuny qtgtwynx snxq zy fqnvznu jc jf htrrtit htsxjvzfy. Iznx fzyjr ajq jzr nwnzwj itqtw ns mjsiwjwny ns azquzyfyj ajqny jxxj rtqjxynj htsxjvzfy, ajq nqqzr itqtwj jz kjzlnfy szqqf kfhnqnxnx fy ajwt jwtx jy fhhzrxfs jy nzxyt tint inlsnxxnr vzn gqfsiny uwfjxjsy qzuyfyzr eewnq ijqjsny fzlzj iznx itqtwj yj kjzlfny szqqf kfhnqnxn. " +
                "Sfr qngjw yjrutw hzr xtqzyf stgnx jqjnkjsi tuynts htslzj snmnq nrujwinjy itrnsl ni vzti rfenr uqfhjwfy kfhjw utxxnr fxxzr. Qtwjr nuxzr itqtw xny frjy, htsxjhyjyzjw finunxhnsl jqny, xji infr stszrrd sngm jznxrti ynshnizsy zy qftwjjy itqtwj rflsf fqnvzfr jwfy atqzyufy. Zy bnxn jsnr fi rnsnr ajsnfr, vznx stxywzi jcjwhn yfynts zqqfrhtwujw xzxhnuny qtgtwynx snxq zy fqnvznu jc jf htrrtit";
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

    private static void fillDBWithChannels() {
        HSQLDB.instance.setupConnection();
        Participant participant01 = new Participant(1, "hkg", ParticipantType.NORMAL);
        Participant participant02 = new Participant(2, "wuh", ParticipantType.NORMAL);
        Channel channel1 = new Channel("hkg_wuh_test01", participant01, participant02);
        Channel channel2 = new Channel("wuh_hkg_test02", participant02, participant01);
        HSQLDB.instance.insertDataTableParticipants(participant01.getName(), HSQLDB.instance.getTypeID(participant01.getType()));
        HSQLDB.instance.insertDataTableParticipants(participant02.getName(), HSQLDB.instance.getTypeID(participant02.getType()));
        HSQLDB.instance.insertDataTableChannel(channel1);
        HSQLDB.instance.insertDataTableChannel(channel2);
        HSQLDB.instance.shutdown();
    }
}