import java.math.BigInteger;

public interface ICryptoEngine {
    String decrypt(String message, BigInteger d, BigInteger n);
    String encrypt(String message, BigInteger e, BigInteger n);
}