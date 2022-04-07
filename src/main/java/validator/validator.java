package validator;

import parser.parser;
import support.Constants;

public class validator {

    public static void validate(String query)
    {
        int i = query.indexOf(' ');
        String firstWord = query.substring(0, i);

        if((query.contains("create") || query.contains("CREATE"))
                && (query.contains("database") || query.contains("DATABASE"))
                && firstWord.equalsIgnoreCase("create"))
        {
            if(query.matches(Constants.createDatabaseRegex))
            {
                parser.parseCreateDatabase(query);
            }
        }

        else if((query.contains("use") || query.contains("USE"))
                && firstWord.equalsIgnoreCase("use"))
        {
            int wordCount = query.split("\\s+").length;
            if(wordCount == 3 && query.matches(Constants.useDatabaseRegex))
            {
                parser.parseUseDatabase(query);
            }
        }

        else if((query.contains("create") || query.contains("CREATE"))
                && (query.contains("table") || query.contains("TABLE"))
                && firstWord.equalsIgnoreCase("create"))
        {
            if(query.matches(Constants.createTableRegex))
            {
                parser.parseCreateTable(query);
            }
        }
    }

}
