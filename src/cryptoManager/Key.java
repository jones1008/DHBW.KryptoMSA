package cryptoManager;

public abstract class Key
{
    protected int n;

    public Key(int n) {
        this.n = n;
    }


    public int getN()
    {
        return n;
    }
}
