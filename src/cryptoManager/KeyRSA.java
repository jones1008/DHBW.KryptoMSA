package cryptoManager;

public class KeyRSA extends Key
{
    private int d;
    private int e;

    public KeyRSA(int n, int d, int e)
    {
        super(n);
        this.d = d;
        this.e = e;
    }

    public int getD()
    {
        return d;
    }

    public int getE()
    {
        return e;
    }
}
