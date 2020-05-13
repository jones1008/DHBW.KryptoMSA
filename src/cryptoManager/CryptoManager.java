package cryptoManager;

import application.Algorithm;
import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;

public class CryptoManager implements ICryptoManager
{
    private Object port;
    private Method cryptoMethod;
    private Method crackMethod;

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

    public String crack(String message, String algorithm)
    {
        setAlgorithm(algorithm);
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
                    crackMethod = port.getClass().getMethod("crack", String.class, int.class); // shfit parameters: String message, int timeout
                    break;
                case RSA:
                    crackMethod = port.getClass().getMethod("crack", BigInteger.class, BigInteger.class, BigInteger.class, int.class); // rsa parameters: BigInteger message, BigInteger e, BigInteger n, int timeout
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String decryptRSA(String message, KeyRSA key)
    {
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
        try {
            return (String) crackMethod.invoke(port, message, Configuration.instance.crackTimeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String crackRSA(String message) {
        BigInteger e = BigInteger.valueOf(12371);
        BigInteger n = new BigInteger("517815623413379");

        try {
            String plainMessage = (String) crackMethod.invoke(port, new BigInteger(message), e, n, Configuration.instance.crackTimeout);
            return plainMessage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
