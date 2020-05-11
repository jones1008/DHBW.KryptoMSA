package companyNetwork;

public interface ICryptoManager {
    String decrypt(String message);
    String encrypt(String message);
    String[] showAlgorithms();
    String crack(String message, int timeout);
}
