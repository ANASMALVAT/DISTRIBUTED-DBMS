package logmanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GeneralLogs {

    public void writeGeneralLogs(long start_time, long end_time, Instant beforeTime, Instant afterTime) throws IOException
    {

        boolean fileExists = false;
        if (Files.exists(Paths.get("General_Logs.txt")))
        {
            fileExists = true;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("General_Logs.txt", true));
        long executionTime = end_time - start_time;
        String formatted_query;
        if(fileExists)
        {
            formatted_query = ", " + "\r\n" + "{" + "\r\n" + "Execution Time of the query (in ms): "  + executionTime + "," + "\r\n"
                    + "Start Time: " +  beforeTime + "," + "\r\n"  + "End Time: " + afterTime + "\r\n" + "}";
        }
        else
        {
            formatted_query = "{" + "\r\n" + "Execution Time of the query (in ms): "  + executionTime + "," + "\r\n"
                    + "Start Time: " +  beforeTime + "," +  "\r\n"  + "End Time: " + afterTime + "\r\n" + "}";
        }
        writer.write(formatted_query);
        writer.close();

    }

}
