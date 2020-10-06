import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;

public class CrackerEngineRSA
{
    BigInteger e;
    BigInteger n;

    private static CrackerEngineRSA instance = new CrackerEngineRSA();

    public Port port;

    private CrackerEngineRSA()
    {
        port = new Port();
    }

    public static CrackerEngineRSA getInstance()
    {
        return instance;
    }

    private String innerMethodDecrypt(String encryptedMessage, File keyfile) throws InterruptedException
    {
        readKeyfile(keyfile);
        BigInteger p, q, d;
        List<BigInteger> factorList = factorize(n);

        if (factorList == null || factorList.size() != 2) {
            throw new InterruptedException("cannot determine factors p and q");
        }

        p = factorList.get(0);
        q = factorList.get(1);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
        BigInteger message = new BigInteger(Base64.getDecoder().decode(encryptedMessage));
        BigInteger plain = message.modPow(d, n);
        if (plain == null) {
            return "";
        }
        byte[] plainBytes = plain.toByteArray();
        return new String(plainBytes);
    }

    private List<BigInteger> factorize(BigInteger n) {
        BigInteger two = BigInteger.valueOf(2);
        List<BigInteger> factorList = new LinkedList<>();

        if (n.compareTo(two) < 0) {
            throw new IllegalArgumentException("must be greater than one");
        }

        while (n.mod(two).equals(BigInteger.ZERO)) {
            factorList.add(two);
            n = n.divide(two);

            if (Thread.currentThread().isInterrupted()){
                return null;
            }
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            BigInteger factor = BigInteger.valueOf(3);
            while (factor.multiply(factor).compareTo(n) <= 0) {
                if (n.mod(factor).equals(BigInteger.ZERO)) {
                    factorList.add(factor);
                    n = n.divide(factor);
                } else {
                    factor = factor.add(two);
                }

                if (Thread.currentThread().isInterrupted()) {
                    return null;
                }
            }
            factorList.add(n);
        }

        return factorList;
    }

    private void readKeyfile(File keyfile) {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(keyfile));
            String currentLine;
            String stringN = "", stringE = "";

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.charAt(0) != '{' && currentLine.charAt(0) != '}')
                {
                    String[] splitted = currentLine.split(":");
                    if (splitted[1].charAt(splitted[1].length()-1) == ',') {
                        splitted[1] = splitted[1].substring(0, splitted[1].length()-1);
                    }

                    if (splitted[0].contains("e"))
                    {
                        stringE = splitted[1].trim(); // trim() removes spaces from String
                    }
                    if (splitted[0].contains("n"))
                    {
                        stringN = splitted[1].trim(); // trim() removes spaces from String
                    }
                }
            }

            e = new BigInteger(stringE);
            n = new BigInteger(stringN);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public class Port implements ICrackerEngine
    {
        public String decrypt(String message, File keyfile) throws InterruptedException
        {
            return innerMethodDecrypt(message, keyfile);
        }
    }
}
