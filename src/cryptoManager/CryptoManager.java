package cryptoManager;

import application.Algorithm;
import configuration.Configuration;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.interfaces.RSAKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.*;

public class CryptoManager implements ICryptoManager
{
    private Object port;
    private Method cryptoMethod;
    private Method crackMethod;

    private boolean debugActive;

    public CryptoManager() {
        this.debugActive = false;
    }

    public CryptoManager(boolean debugActive) {
        this.debugActive = debugActive;
    }

    public String decrypt(String message, String algorithm, String keyfile)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }

        createCryptoMethod("decrypt");

        return crypt(message, new File(Configuration.instance.keyfilesDirectory + keyfile));
    }

    public String encrypt(String message, String algorithm, String keyfile)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }

        createCryptoMethod("encrypt");

        return crypt(message, new File(Configuration.instance.keyfilesDirectory + keyfile));
    }

    public String[] showAlgorithms()
    {
        String[] algorithms = new String[Algorithm.values().length];
        for (int i = 0; i < Algorithm.values().length; i++)
        {
            algorithms[i] = Algorithm.values()[i].toString();
        }
        return algorithms;
    }

    public String crack(String message, String algorithm)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }

        createCrackMethod();

        String result = "";

        switch (Configuration.instance.algorithm) {
            case SHIFT:
                result = crackShift(message);
                break;
            case RSA:
                result = crackRSA(message);
                break;
        }

        return result;
    }


    private boolean setAlgorithm(String algorithm)
    {
        switch (algorithm.toLowerCase()) {
            case "shift":
                Configuration.instance.algorithm = Algorithm.SHIFT;
                break;
            case "rsa":
                Configuration.instance.algorithm = Algorithm.RSA;
                break;
            default:
                return false;
        }
        return true;
    }

    private void createCryptoMethod(String methodType) { // methodType: decrypt or encrypt
        Object instance;

        try {
            URL[] urls = {new File(Configuration.instance.getComponentPath(false)).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoManager.class.getClassLoader());
            Class clazz = Class.forName("CryptoEngine" + Configuration.instance.algorithm.toString(), true, urlClassLoader);

            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);

            cryptoMethod = port.getClass().getMethod(methodType, String.class,File.class); // parameters: String message, File keyfile
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCrackMethod() {
        Object instance;
        try {
            URL[] urls = {new File(Configuration.instance.getComponentPath(true)).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoManager.class.getClassLoader());
            Class clazz = Class.forName("CrackerEngine" + Configuration.instance.algorithm.toString(), true, urlClassLoader);

            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);

            switch (Configuration.instance.algorithm) {
                case SHIFT:
                    crackMethod = port.getClass().getMethod("decrypt", String.class); // shfit parameters: String message
                    break;
                case RSA:
                    crackMethod = port.getClass().getMethod("decrypt", BigInteger.class, File.class); // rsa parameters: BigInteger message, BigInteger e, BigInteger n
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String crypt(String message, File keyfile)
    {
        try
        {
            return (String) cryptoMethod.invoke(port, message, keyfile);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private String crackShift(String message) {
        String failedString = "cracking encrypted message \"" + message + "\" failed";
        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            List<Future<String>> future = executor.invokeAll(Arrays.asList(new ShiftCrackTask(message, crackMethod, port)), Configuration.instance.crackTimeout, TimeUnit.SECONDS);
            executor.shutdown();
            if (future.get(0).isCancelled()) {
                return failedString;
            }
            return future.get(0).get();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return failedString;
        }
    }

    private String crackRSA(String message) {
        File[] rsaKeyfiles = new File(Configuration.instance.keyfilesDirectory).listFiles((dir, name) -> name.toLowerCase().startsWith("rsa"));
        List<Callable<String>> tasks = new ArrayList<>();
        for (File file : rsaKeyfiles) {
            tasks.add(new RSACrackTask(message, file, crackMethod, port));
        }

        String failedString = "cracking encrypted message \"" + message + "\" failed";
        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            List<Future<String>> futures = executor.invokeAll(tasks, Configuration.instance.crackTimeout, TimeUnit.SECONDS);
            executor.shutdown();

            for (Future<String> future : futures) {
                if (!future.isCancelled()) {
                    return future.get();
                }
            }

            return failedString;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return failedString;
        }
    }
}
