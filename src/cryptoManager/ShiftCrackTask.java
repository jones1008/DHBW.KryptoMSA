package cryptoManager;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ShiftCrackTask implements Callable<String>
{
    private String message;

    private Object port;
    private Method crackMethod;

    public ShiftCrackTask(String message, Method method, Object port)
    {
        this.message = message;
        this.crackMethod = method;
        this.port = port;
    }

    @Override
    public String call() throws Exception
    {
        try {
            return (String) crackMethod.invoke(port, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
