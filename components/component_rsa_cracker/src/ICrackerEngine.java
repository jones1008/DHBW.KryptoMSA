import java.io.File;
import java.math.BigInteger;

public interface ICrackerEngine
{
    String decrypt(String message, File keyfile);
}