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

    private String innerMethodDecrypt(byte[] message, BigInteger d, BigInteger n) {
        byte[] msg = crypt(new BigInteger(message), d, n).toByteArray();
        return new String(msg);
    }

    private byte[] innerMethodEncrypt(String plainMessage, BigInteger e, BigInteger n) {
        byte[] bytes = plainMessage.getBytes(Charset.defaultCharset());
        BigInteger bytesBigInteger = new BigInteger(bytes);
        BigInteger cipher = crypt(bytesBigInteger, e, n);
        return cipher.toByteArray();
    }

    private BigInteger crypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public class Port implements ICryptoEngine
    {

        public String decrypt(String message, BigInteger d, BigInteger n)
        {
            return innerMethodDecrypt(Base64.getDecoder().decode(message), d, n);
        }

        public String encrypt(String message, BigInteger e, BigInteger n)
        {
            return Base64.getEncoder().encodeToString(innerMethodEncrypt(message, e, n));
        }
    }
}
