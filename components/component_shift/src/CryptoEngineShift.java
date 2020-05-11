import java.math.BigInteger;
import java.nio.charset.Charset;

public class CryptoEngineShift
{

    private static CryptoEngineShift instance = new CryptoEngineShift();

    public Port port;

    private CryptoEngineShift()
    {
        port = new Port();
    }

    public static CryptoEngineShift getInstance()
    {
        return instance;
    }

    private String innerMethodDecrypt(String message, int key) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char character = (char) (message.codePointAt(i) - key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private String innerMethodEncrypt(String message, int key) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char character = (char) (message.codePointAt(i) + key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    public class Port implements ICryptoEngine
    {

        public String decrypt(String message, int key)
        {
            return innerMethodDecrypt(message, key);
        }

        public String encrypt(String message, int key)
        {
            return innerMethodEncrypt(message, key);
        }
    }
}
