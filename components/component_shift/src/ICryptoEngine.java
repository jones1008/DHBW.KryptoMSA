import java.math.BigInteger;

public interface ICryptoEngine {
    String decrypt(String message, int key);
    String encrypt(String message, int key);
}