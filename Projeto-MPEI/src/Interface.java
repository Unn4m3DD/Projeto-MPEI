import util.BookDirectoryProcessor;
import util.Mutable;
import util.ProcessedBooksResult;

import java.io.File;
import java.util.HashMap;

public class Interface {
    public HashMap<String, ProcessedBooksResult> currentData;
    public File currentDir;

    public void parseDirectory(Mutable<Double> progress) {
        currentData = (new BookDirectoryProcessor(currentDir, progress)).result;
    }
}
