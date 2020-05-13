package cryptoManager;

import java.math.BigInteger;

public class KeyRSA extends Key
{
    private BigInteger n;
    private BigInteger d;
    private BigInteger e;

    public KeyRSA(String n, String d, String e)
    {
        super();
        this.n = new BigInteger(n);
        this.d = new BigInteger(d);
        this.e = new BigInteger(e);
    }

    public BigInteger getN()
    {
        return n;
    }

    public BigInteger getD()
    {
        return d;
    }

    public BigInteger getE()
    {
        return e;
    }
}
