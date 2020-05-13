import java.math.BigInteger;

public interface ICryptoEngine {
    String decrypt(String message, BigInteger key);
    String encrypt(String message, BigInteger key);
}