package cryptoManager;

import application.Algorithm;
import configuration.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;

public class KeyReader implements IKeyReader
{
    public Key readKey(String keyfile, Algorithm algorithm)
    {
        Key key = null;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(Configuration.instance.keyfilesDirectory + keyfile));
            String currentLine;
            int d = 0, e = 0, n = 0, shiftKey = 0;

            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.charAt(0) != '{' && currentLine.charAt(0) != '}')
                {
                    String[] splitted = currentLine.split(":");
                    if (splitted[1].charAt(splitted[1].length()-1) == ',') {
                        splitted[1] = splitted[1].substring(0, splitted[1].length()-1);
                    }

                    if (algorithm == Algorithm.RSA)
                    {
                        if (splitted[0].contains("d"))
                        {
                            d = Integer.parseInt(splitted[1].trim()); // trim() removes spaces from String
                        }
                        if (splitted[0].contains("e"))
                        {
                            e = Integer.parseInt(splitted[1].trim()); // trim() removes spaces from String
                        }
                        if (splitted[0].contains("n"))
                        {
                            n = Integer.parseInt(splitted[1].trim()); // trim() removes spaces from String
                        }
                    }

                    if (algorithm == Algorithm.SHIFT)
                    {
                        if (splitted[0].contains("key"))
                        {
                            shiftKey = Integer.parseInt(splitted[1].trim());
                        }
                    }
                }
            }
            switch (algorithm)
            {
                case SHIFT:
                    key = new KeyShift(shiftKey);
                    break;
                case RSA:
                    key = new KeyRSA(n, d, e);
                    break;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return key;
    }
}
