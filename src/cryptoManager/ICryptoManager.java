package cryptoManager;

public interface ICryptoManager
{
    String decrypt(String message, String algorithm, String keyfile);
    String encrypt(String message, String algorithm, String keyfile);
    String[] showAlgorithms();
    String crack(String message, int timeout);
}
