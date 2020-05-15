import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CryptoEngineSHIFT
{
    private static CryptoEngineSHIFT instance = new CryptoEngineSHIFT();

    public Port port;

    private CryptoEngineSHIFT()
    {
        port = new Port();
    }

    public static CryptoEngineSHIFT getInstance()
    {
        return instance;
    }

    private String innerMethodDecrypt(String message, File keyfile) {
        int key = readKeyfile(keyfile);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char character = (char) (message.codePointAt(i) - key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private String innerMethodEncrypt(String message, File keyfile) {
        int key = readKeyfile(keyfile);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            char character = (char) (message.codePointAt(i) + key);
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private int readKeyfile(File keyfile) {
        int key = 0;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(keyfile));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.matches(".* \"key\": .*"))
                {
                    String[] splitted = currentLine.split(":");
                    key = Integer.parseInt(splitted[1].trim());
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return key;
    }

    public class Port implements ICryptoEngine
    {

        public String decrypt(String message, File keyfile)
        {
            return innerMethodDecrypt(message, keyfile);
        }

        public String encrypt(String message, File keyfile)
        {
            return innerMethodEncrypt(message, keyfile);
        }
    }
}
