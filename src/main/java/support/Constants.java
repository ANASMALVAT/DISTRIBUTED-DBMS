package support;

public class Constants{
    public static final String createDatabaseRegex  = "(CREATE|create)\\s+(DATABASE|database)\\s+\\w+\\s*(;)";
    public static final String createTableRegex = "(CREATE|create)\\s+(TABLE|table)\\s*\\w+\\((.*?)\\)(;)";
    public static final String useDatabaseRegex = "(use|USE)\\s+\\w+\\s*(;)";

    public static final String outputFolderPath = "./output/";
}
