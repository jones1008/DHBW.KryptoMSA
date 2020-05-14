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

    private IKeyReader keyReader;

    public CryptoManager() {
        this.keyReader = new KeyReader();
        this.debugActive = false;
    }

    public CryptoManager(boolean debugActive) {
        this.keyReader = new KeyReader();
        this.debugActive = debugActive;
    }

    public String decrypt(String message, String algorithm, String keyfile)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }

        createCryptoMethod("decrypt");

        String result = "";

        switch (Configuration.instance.algorithm)
        {
            case RSA:
                KeyRSA keyRSA = (KeyRSA) keyReader.readKey(keyfile, Algorithm.RSA);
                result = decryptRSA(message, keyRSA);
                break;
            case SHIFT:
                KeyShift keyShift = (KeyShift) keyReader.readKey(keyfile, Algorithm.SHIFT);
                result = cryptShift(message, keyShift);
                break;
        }
        return result;
    }

    public String encrypt(String message, String algorithm, String keyfile)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }

        createCryptoMethod("encrypt");

        String result = "";

        switch (Configuration.instance.algorithm)
        {
            case RSA:
                KeyRSA keyRSA = (KeyRSA) keyReader.readKey(keyfile, Algorithm.RSA);
                result = encryptRSA(message, keyRSA);
                break;
            case SHIFT:
                KeyShift keyShift = (KeyShift) keyReader.readKey(keyfile, Algorithm.SHIFT);
                result = cryptShift(message, keyShift);
                break;
        }

        return result;
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

            switch (Configuration.instance.algorithm) {
                case SHIFT:
                    cryptoMethod = port.getClass().getMethod(methodType, String.class, int.class); // parameters: String message, int key
                    break;
                case RSA:
                    cryptoMethod = port.getClass().getMethod(methodType, String.class, BigInteger.class, BigInteger.class); // parameters: String message, int d/e, int n
                    break;
            }
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
                    crackMethod = port.getClass().getMethod("decrypt", BigInteger.class, BigInteger.class, BigInteger.class); // rsa parameters: BigInteger message, BigInteger e, BigInteger n
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String decryptRSA(String message, KeyRSA key)
    {
        if (key == null) {
            return "Key not found";
        }
        try
        {
            //return CryptoEngineRSA.decrypt(message, key.getD(), key.getN());
            return (String) cryptoMethod.invoke(port, message, key.getD(), key.getN());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private String encryptRSA(String message, KeyRSA key)
    {
        if (key == null) {
            return "Key not found";
        }
        try
        {
            //return CryptoEngineRSA.encrypt(message, key.getE(), key.getN());
            return (String) cryptoMethod.invoke(port, message, key.getE(), key.getN());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private String cryptShift(String message, KeyShift key)
    {
        if (key == null) {
            return "Key not found";
        }
        try
        {
            return (String) cryptoMethod.invoke(port, message, key.getN());
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
            KeyRSA key = (KeyRSA) keyReader.readKey(file.getName(), Algorithm.RSA);
            tasks.add(new RSACrackTask(message, key, crackMethod, port));
        }

        String failedString = "cracking encrypted message \"" + message + "\" failed";
        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            List<Future<String>> future = executor.invokeAll(tasks, Configuration.instance.crackTimeout, TimeUnit.SECONDS);
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
}
