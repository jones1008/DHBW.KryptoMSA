package cryptoManager;

import java.math.BigInteger;

public class KeyRSA extends Key
{
    private BigInteger d;
    private BigInteger e;

    public KeyRSA(BigInteger n, BigInteger d, BigInteger e)
    {
        super(n);
        this.d = d;
        this.e = e;
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
