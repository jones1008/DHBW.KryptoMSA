import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Base64;

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

    private byte[] innerMethodEncrypt(String plainMessage, int e, int n) {
        byte[] bytes = plainMessage.getBytes();
        return crypt(new BigInteger(bytes), BigInteger.valueOf(e), BigInteger.valueOf(n)).toByteArray();
    }

    private BigInteger crypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public class Port implements ICryptoEngine
    {

        public String decrypt(String message, int d, int n)
        {
            return innerMethodDecrypt(Base64.getDecoder().decode(message), d, n);
        }

        public String encrypt(String message, int e, int n)
        {
            return Base64.getEncoder().encodeToString(innerMethodEncrypt(message, e, n));
        }
    }
}
