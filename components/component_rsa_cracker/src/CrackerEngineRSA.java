import java.math.BigInteger;
import java.util.*;

public class CrackerEngineRSA
{
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

    private String innerMethodDecrypt(BigInteger message, BigInteger e, BigInteger n) {
        BigInteger p, q, d;
        List<BigInteger> factorList = factorize(n);

        if (factorList.size() != 2) {
            return "cannot determine factors p and q";
        }

        p = factorList.get(0);
        q = factorList.get(1);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        d = e.modInverse(phi);
        return new String(message.modPow(d, n).toByteArray());
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
            }
            factorList.add(n);
        }

        return factorList;
    }

    public class Port implements ICrackerEngine
    {
        public String decrypt(BigInteger message, BigInteger e, BigInteger n)
        {
            return innerMethodDecrypt(message, e, n);
        }
    }
}
