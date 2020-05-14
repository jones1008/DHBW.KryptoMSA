import java.math.BigInteger;

public interface ICrackerEngine
{
    String decrypt(BigInteger message, BigInteger e, BigInteger n);
}