package cryptoManager;

import java.io.File;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.Callable;

public class RSACrackTask implements Callable<String>
{
    private String message;
    private File key;

    private Object port;
    private Method crackMethod;

    public RSACrackTask(String message, File key, Method method, Object port)
    {
        this.message = message;
        this.key = key;

        this.crackMethod = method;
        this.port = port;
    }

    @Override
    public String call()
    {
        try {
            return (String) crackMethod.invoke(port, message, key);
        } catch (InvocationTargetException ex) {
            System.out.println("Cracking is canceled");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
