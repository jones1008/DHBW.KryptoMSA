package cryptoManager;

import application.Algorithm;
import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CryptoManager implements ICryptoManager
{
    private Object port;
    private Method cryptoMethod;
    private Method crack;

    private IKeyReader keyReader;

    public CryptoManager() {
        this.keyReader = new KeyReader();
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
        return new String[0];
    }

    public String crack(String message, int timeout)
    {
        try {
            return (String) this.crack.invoke(message, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            URL[] urls = {new File(Configuration.instance.getComponentPath()).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoManager.class.getClassLoader());
            Class clazz = Class.forName("CryptoEngine" + Configuration.instance.algorithm.toString(), true, urlClassLoader);

            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);

            switch (Configuration.instance.algorithm) {
                case SHIFT:
                    cryptoMethod = port.getClass().getMethod(methodType, String.class, int.class); // parameters: String message, int key
                    break;
                case RSA:
                    cryptoMethod = port.getClass().getMethod(methodType, String.class, int.class, int.class); // parameters: String message, int d/e, int n
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Method createCrackMethod() {
        Object instance;
        try {
            URL[] urls = {new File(Configuration.instance.getComponentPath()).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, companyNetwork.CryptoManager.class.getClassLoader());
            Class clazz = Class.forName(Configuration.instance.algorithm.toString(), true, urlClassLoader);
            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);
            return port.getClass().getMethod("crack", String.class, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decryptRSA(String message, KeyRSA key)
    {
        try
        {
            return (String) cryptoMethod.invoke(port, message, key.getD(), key.getN());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private String encryptRSA(String message, KeyRSA key)
    {
        try
        {
            return (String) cryptoMethod.invoke(port, message, key.getE(), key.getN());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private String cryptShift(String message, KeyShift key)
    {
        try
        {
            return (String) cryptoMethod.invoke(port, message, key.getN());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}