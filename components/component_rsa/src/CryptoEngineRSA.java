import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;

public class CryptoEngineRSA
{
    private static CryptoEngineRSA instance = new CryptoEngineRSA();
    private BigInteger d, e, n;

    public Port port;

    private CryptoEngineRSA()
    {
        port = new Port();
    }

    public static CryptoEngineRSA getInstance()
    {
        return instance;
    }

    private String innerMethodDecrypt(byte[] message, File keyfile) {
        readKeyfile(keyfile);
        byte[] msg = crypt(new BigInteger(message), d, n).toByteArray();
        return new String(msg);
    }

    private byte[] innerMethodEncrypt(String plainMessage, File keyfile) {
        readKeyfile(keyfile);
        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        BigInteger bytesBigInteger = new BigInteger(bytes);
        BigInteger cipher = crypt(bytesBigInteger, e, n);
        return cipher.toByteArray();
    }

    private BigInteger crypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    private void readKeyfile(File keyfile) {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(keyfile));
            String currentLine;
            String stringN = "", stringD = "", stringE = "";

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.charAt(0) != '{' && currentLine.charAt(0) != '}')
                {
                    String[] splitted = currentLine.split(":");
                    if (splitted[1].charAt(splitted[1].length()-1) == ',') {
                        splitted[1] = splitted[1].substring(0, splitted[1].length()-1);
                    }

                    if (splitted[0].contains("d"))
                    {
                        stringD = splitted[1].trim(); // trim() removes spaces from String
                    }
                    if (splitted[0].contains("e"))
                    {
                        stringE = splitted[1].trim(); // trim() removes spaces from String
                    }
                    if (splitted[0].contains("n"))
                    {
                        stringN = splitted[1].trim(); // trim() removes spaces from String
                    }
                }
            }

            d = new BigInteger(stringD);
            e = new BigInteger(stringE);
            n = new BigInteger(stringN);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public class Port implements ICryptoEngine
    {

        public String decrypt(String message, File keyfile)
        {
            return innerMethodDecrypt(Base64.getDecoder().decode(message), keyfile);
        }

        public String encrypt(String message, File keyfile)
        {
            return Base64.getEncoder().encodeToString(innerMethodEncrypt(message, keyfile));
        }
    }
}
