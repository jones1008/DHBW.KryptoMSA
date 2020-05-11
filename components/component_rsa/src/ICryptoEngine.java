public interface ICryptoEngine {
    String decrypt(String message, int d, int n);
    String encrypt(String message, int e, int n);
}