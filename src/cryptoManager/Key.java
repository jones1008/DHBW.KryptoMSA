package cryptoManager;

import java.math.BigInteger;

public abstract class Key
{
    protected BigInteger n;

    public Key(BigInteger n) {
        this.n = n;
    }


    public BigInteger getN()
    {
        return n;
    }
}
