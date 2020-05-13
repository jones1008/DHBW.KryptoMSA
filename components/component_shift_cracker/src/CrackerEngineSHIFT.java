import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CrackerEngineSHIFT
{
    private static CrackerEngineSHIFT instance = new CrackerEngineSHIFT();

    public Port port;

    private CrackerEngineSHIFT()
    {
        port = new Port();
    }

    public static CrackerEngineSHIFT getInstance()
    {
        return instance;
    }

    private String innerMethodCrack(String message, int timeout) {
        String source = message.toUpperCase();
        char[] sourceText = new char[source.length()];
        for (int i = 0; i < source.length(); i++)
        {
            sourceText[i] = source.charAt(i);
        }

        int[] unicode = new int[source.length()];
        int[] unicodeCopy = new int[source.length()];
        String hex;
        int dec;

        for (int count = 0; count < sourceText.length; count++) {
            hex = Integer.toHexString(sourceText[count]);
            dec = Integer.parseInt(hex, 16);
            unicode[count] = dec;
            unicodeCopy[count] = dec;
        }

        StringBuilder possibleResults = new StringBuilder();

        for (int shift = 1; shift <= 25; shift++) {
            String result = smartShift(shift, unicode, unicodeCopy);
            if (!result.isEmpty()) {
                possibleResults.append(result);
                possibleResults.append(System.getProperty("line.separator"));
            }
        }

        return possibleResults.toString();
    }

    private String smartShift(int shift, int[] unicode, int[] unicodeCopy)
    {
        for (int x = 0; x <= unicode.length - 1; x++) {
            unicodeCopy[x] = unicode[x];

            if (unicode[x] >= 65 && unicode[x] <= 90) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 90) {
                    unicodeCopy[x] -= 26;
                }
            }
        }

        String[] processed = new String[unicode.length];
        char[] finalProcess = new char[unicode.length];

        for (int count = 0; count < processed.length; count++) {
            processed[count] = Integer.toHexString(unicodeCopy[count]);
            int hexToInt = Integer.parseInt(processed[count], 16);
            char intToChar = (char) hexToInt;
            finalProcess[count] = intToChar;
        }

        double frequency = 0;
        double aFrequency = 0;
        double eFrequency = 0;
        double iFrequency = 0;
        double oFrequency = 0;
        double uFrequency = 0;

        for (char c : finalProcess) {
            frequency++;

            switch (c) {
                case 'A':
                    aFrequency++;
                    break;
                case 'E':
                    eFrequency++;
                    break;
                case 'I':
                    iFrequency++;
                    break;
                case 'O':
                    oFrequency++;
                    break;
                case 'U':
                    uFrequency++;
                    break;
                default:
                    break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char character : finalProcess) {
            stringBuilder.append(character);
        }

        if (eFrequency / frequency >= 0.05 || aFrequency / frequency >= 0.05 || iFrequency / frequency >= 0.05 || oFrequency / frequency >= 0.05 || uFrequency / frequency >= 0.05) {
            return stringBuilder.toString();
        }
        return "";
    }

    public class Port implements ICrackerEngine
    {

        public String crack(String message, int timeout)
        {
            return innerMethodCrack(message, timeout);
        }
    }
}
