import datadump.datadumpCreator;
import erd.erdCreator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        erdCreator.createERDDiagram("u1", "db1");
        datadumpCreator.createDataDump("u1", "db1");
    }
}
