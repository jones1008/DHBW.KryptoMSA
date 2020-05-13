import java.math.BigInteger;

public interface ICrackerEngine
{
    String crack(BigInteger message, BigInteger e, BigInteger n, int timeout);
}