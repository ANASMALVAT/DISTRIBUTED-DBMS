package parser;

import support.Constants;
import java.io.File;

public class parser {

    public static void parseCreateDatabase(String query)
    {
        System.out.println("hi");
        String lowerCaseQuery = query.toLowerCase();
        int startIndex = lowerCaseQuery.indexOf("database");
        int endIndex = lowerCaseQuery.indexOf(";");
        String dbName = query.substring(startIndex + 8,endIndex).trim();
        String path = Constants.outputFolderPath;
        File dbDirectory = new File(path+dbName);
        boolean isDBCreated = dbDirectory.mkdir();
        if(isDBCreated)
        {
            System.out.println("Created");
        }
        else
        {

        }
    }

    public static void parseCreateTable(String query)
    {

    }

    public static void parseUseDatabase(String query)
    {

    }
}
