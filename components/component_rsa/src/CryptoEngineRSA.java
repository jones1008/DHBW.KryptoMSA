import java.math.BigInteger;
import java.nio.charset.Charset;

public class CryptoEngineRSA
{

    private static CryptoEngineRSA instance = new CryptoEngineRSA();

    public Port port;

    private CryptoEngineRSA()
    {
        port = new Port();
    }

    public static CryptoEngineRSA getInstance()
    {
        return instance;
    }

    private String innerMethodDecrypt(byte[] message, int d, int n) {
        byte[] msg = crypt(new BigInteger(message), BigInteger.valueOf(d), BigInteger.valueOf(n)).toByteArray();
        return new String(msg);
    }

    private String innerMethodEncrypt(String message, int e, int n) {
        byte[] bytes = message.getBytes(Charset.defaultCharset());
        return crypt(new BigInteger(bytes), BigInteger.valueOf(e), BigInteger.valueOf(n)).toByteArray().toString();
    }

    private BigInteger crypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public class Port implements ICryptoEngine
    {

        public String decrypt(String message, int d, int n)
        {
            return innerMethodDecrypt(message.getBytes(), d, n);
        }

        public String encrypt(String message, int e, int n)
        {
            return innerMethodEncrypt(message, e, n);
        }
    }
}
