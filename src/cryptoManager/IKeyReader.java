package cryptoManager;

import application.Algorithm;

public interface IKeyReader
{
    Key readKey(String keyfile, Algorithm algorithm);
}
