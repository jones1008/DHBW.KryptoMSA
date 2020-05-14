import java.io.File;
import java.math.BigInteger;

public interface ICrackerEngine
{
    String decrypt(BigInteger message, File keyfile);
}