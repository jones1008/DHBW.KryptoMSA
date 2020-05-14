package cryptoManager;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Base64;
import java.util.concurrent.Callable;

public class RSACrackTask implements Callable<String>
{
    private String message;
    private KeyRSA key;

    private Object port;
    private Method crackMethod;

    public RSACrackTask(String message, KeyRSA key, Method method, Object port)
    {
        this.message = message;
        this.key = key;

        this.crackMethod = method;
        this.port = port;
    }

    @Override
    public String call() throws Exception
    {
        BigInteger messageInt = new BigInteger(Base64.getDecoder().decode(message));

        try {
            return (String) crackMethod.invoke(port, messageInt, key.getE(), key.getN());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
