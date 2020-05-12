package companyNetwork;

import configuration.Configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class CryptoManager implements ICryptoManager {

    private Object port;
    private Method decrypt;
    private Method encrypt;
    private Method crack;

    private void createDecryptMethod() {
        this.decrypt = this.createMethod("decrypt");
    }
    private void createEncryptMethod() {
        this.encrypt = this.createMethod("encrypt");
    }
    private void createCrackMethod() {
        this.crack = this.createMethod("crack");
    }
    private Method createMethod(String methodName) {
        Object instance;
        try {
            URL[] urls = {new File(Configuration.instance.getComponentPath()).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CryptoManager.class.getClassLoader());
            Class clazz = Class.forName(Configuration.instance.algorithm.toString(), true, urlClassLoader);
            instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);
            return port.getClass().getMethod(methodName, String.class, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String message) {
        try {
            return (String) this.decrypt.invoke(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encrypt(String message) {
        try {
            return (String) this.encrypt.invoke(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String crack(String message, int timeout) {
        try {
            return (String) this.crack.invoke(message, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] showAlgorithms() {
        return new String[0];
    }
}
