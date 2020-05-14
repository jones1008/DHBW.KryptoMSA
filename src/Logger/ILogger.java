package Logger;

import java.io.File;

public interface ILogger
{
    File getLatestLogfile();
    void log(String text);
}
