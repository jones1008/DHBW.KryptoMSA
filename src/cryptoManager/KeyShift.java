package cryptoManager;

public class KeyShift extends Key
{
    private int n;

    public KeyShift(int n)
    {
        this.n = n;
    }

    public int getN() {
        return n;
    }
}
