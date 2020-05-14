import java.io.File;

public interface ICryptoEngine {
    String decrypt(String message, File keyfile);
    String encrypt(String message, File keyfile);
}