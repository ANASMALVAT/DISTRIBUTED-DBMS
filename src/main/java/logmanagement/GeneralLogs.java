package logmanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GeneralLogs {

    public void writeGeneralLogs(long start_time, long end_time) throws IOException
    {
        File file = new File("General_Logs.txt");
        boolean fileExists = false;
        if (file.exists())
        {
            fileExists = true;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("General_Logs.txt"));
        long executionTime = end_time - start_time;

        String formatted_query;
        if(fileExists)
        {
            formatted_query = ",{  " + "\r\n" + "Execution Time of the query in general logs: "  + executionTime + "\r\n" + "}";
        }
        else
        {
            formatted_query = "{  " + "\r\n" + "Execution Time of the query in general logs: "  + executionTime + "\r\n" + "}";
        }
        writer.write(formatted_query);
        writer.close();

    }
    
}
