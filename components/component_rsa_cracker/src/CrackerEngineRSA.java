import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

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

    private String innerMethodCrack(BigInteger message, BigInteger e, BigInteger n, int timeout) {
        String failedString = "cracking encrypted message \"" + message + "\" failed";
        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            List<Future<BigInteger>> future = executor.invokeAll(Arrays.asList(new CrackTask(message, e, n)), timeout, TimeUnit.SECONDS);
            executor.shutdown();
            if (future.get(0).isCancelled()) {
                return failedString;
            }
            return future.get(0).get().toString();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return failedString;
        }
    }

    class CrackTask implements Callable<BigInteger> {
        private BigInteger message;
        private BigInteger e;
        private BigInteger n;

        public CrackTask(BigInteger message, BigInteger e, BigInteger n) {
            this.message = message;
            this.e = e;
            this.n = n;
        }

        @Override
        public BigInteger call() throws Exception {
            BigInteger p, q, d;
            List<BigInteger> factorList = factorize(n);

            if (factorList.size() != 2) {
                throw new Exception("cannot determine factors p and q");
            }

            p = factorList.get(0);
            q = factorList.get(1);
            BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
            d = e.modInverse(phi);
            return message.modPow(d, n);
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
    }



    public class Port implements ICrackerEngine
    {
        public String crack(BigInteger message, BigInteger e, BigInteger n, int timeout)
        {
            return innerMethodCrack(message, e, n, timeout);
        }
    }
}
