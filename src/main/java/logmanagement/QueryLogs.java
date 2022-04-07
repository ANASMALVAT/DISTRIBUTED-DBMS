package logmanagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QueryLogs {

    public void writeQueryLogs(long start_time, long end_time) throws IOException
    {
        //File file = new File("Query_Logs.txt");

        boolean fileExists = false;
        if (Files.exists(Paths.get("QueryLogs.txt")))

        {
            fileExists = true;
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("Query_Logs.txt"));
        long executionTime = end_time - start_time;

        String formatted_query;
        if(fileExists)
        {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            formatted_query = "timestamp" + "\r\n" + timestamp
                     + ",{  " + "\r\n" + "Execution Time of the query logs: "  + executionTime + "\r\n" + "}";
        }
        else
        {
             formatted_query = "{  " + "\r\n" + "Execution Time of the query logs: "  + executionTime + "\r\n" + "}";
        }
        writer.write(formatted_query);
        writer.close();


    }

}
