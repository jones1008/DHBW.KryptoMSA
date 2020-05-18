package cryptoManager;

import logger.ILogger;
import application.Algorithm;
import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CryptoManager implements ICryptoManager
{
    private Object port;
    private Method cryptoMethod;
    private Method crackMethod;

    private ILogger logger = null;

    public String decrypt(String message, String algorithm, String keyfile)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }

        log("Creating decryption method at runtime from component");
        createCryptoMethod("decrypt");

        log("Detected decryption algorithm '" + Configuration.instance.algorithm + "'");
        String decryptedMessage = crypt(message, new File(Configuration.instance.keyfilesDirectory + keyfile));
        if (!decryptedMessage.equals("")) {
            log("Successfully decrypted message '" + message + "' to '" + decryptedMessage + "'");
        }
        return decryptedMessage;
    }

    public String encrypt(String message, String algorithm, String keyfile)
    {
        if (!setAlgorithm(algorithm))
        {
            return null;
        }
        log("Creating encryption method at runtime from component");
        createCryptoMethod("encrypt");

        log("Detected encryption algorithm '" + Configuration.instance.algorithm + "'");
        String encryptedMessage = crypt(message, new File(Configuration.instance.keyfilesDirectory + keyfile));
        if (!encryptedMessage.equals("")) {
            log("Successfully encrypted message '" + message + "' to '" + encryptedMessage + "'");
        }
        return encryptedMessage;
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
            log("Starting decryption");
            return (String) cryptoMethod.invoke(port, message, keyfile);
        } catch (Exception e)
        {
            e.printStackTrace();
            log("Error while decryption: " + e.getMessage());
        }
        return "";
    }

    private String crackShift(String message) {
        String failedString = "Error: cracking encrypted message \"" + message + "\" failed";
        try
        {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            List<Future<String>> future = executor.invokeAll(Collections.singletonList(new ShiftCrackTask(message, crackMethod, port)), Configuration.instance.crackTimeout, TimeUnit.SECONDS);
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
        if (rsaKeyfiles == null) {
            return "Error reading keyfiles";
        }
        for (File file : rsaKeyfiles) {
            tasks.add(new RSACrackTask(message, file, crackMethod, port));
        }

        String failedString = "Error: cracking encrypted message \"" + message + "\" failed";
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

    public void setLogger(ILogger logger) {
        this.logger = logger;
    }

    private void log(String text) {
        if (logger != null) {
            logger.log(text);
        }
    }
}
