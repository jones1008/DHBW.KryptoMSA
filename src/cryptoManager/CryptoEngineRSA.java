package cryptoManager;

import java.math.BigInteger;
import java.util.Base64;

public class CryptoEngineRSA
{
    private static String innerMethodDecrypt(byte[] message, int d, int n) {
        byte[] msg = crypt(new BigInteger(message), BigInteger.valueOf(d), BigInteger.valueOf(n)).toByteArray();
        return new String(msg);
    }

    private static byte[] innerMethodEncrypt(String plainMessage, int e, int n) {
        byte[] bytes = plainMessage.getBytes();
        BigInteger bytesBigInteger = new BigInteger(bytes);
        BigInteger cipher = crypt(bytesBigInteger, BigInteger.valueOf(e), BigInteger.valueOf(n));
        return cipher.toByteArray();
    }

    private static BigInteger crypt(BigInteger message, BigInteger e, BigInteger n) {
        return message.modPow(e, n);
    }

    public static String decrypt(String message, int d, int n)
    {
        return innerMethodDecrypt(Base64.getDecoder().decode(message), d, n);
    }

    public static String encrypt(String message, int e, int n)
    {
        byte[] result = innerMethodEncrypt(message, e, n);
        return Base64.getEncoder().encodeToString(result);
    }
}
