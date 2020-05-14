import java.io.File;

public interface ICryptoEngine {
    String decrypt(String message, File keyFile);
    String encrypt(String message, File keyFile);
}