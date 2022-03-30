package datadump;

import support.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class datadumpCreator {
    public static void createDataDump(String username, String databaseName) throws IOException {
        String dataDictionaryPath = Constants.outputFolderPath  + databaseName + "/" + Constants.dataDictionaryFileName;
        String sqlDumpPath = Constants.outputFolderPath  + databaseName + "/" + Constants.sqlDumpFile;

        File sql_dump_file = new File(sqlDumpPath);
        if(sql_dump_file.exists() && !sql_dump_file.isDirectory()) {
            // delete the sql dump file if exists already
            sql_dump_file.delete();
        }
        else
        {
            // create an empty erd file
            sql_dump_file.createNewFile();
        }

        File data_dictionary_file = new File(dataDictionaryPath);
        if(data_dictionary_file.exists() && !data_dictionary_file.isDirectory() && data_dictionary_file.length() > 0) {
            // data dictionary file exists for sql dump creation
        }
        else
        {
            // raise error and terminate and log
        }

        Scanner fileReader_DataDictionary = new Scanner(data_dictionary_file);
        FileWriter fileWriter_SQLDumpFile = new FileWriter(sql_dump_file);
        String lineInDataDictionary;
        boolean firstLine = true;

        // Database queries
        fileWriter_SQLDumpFile.append("CREATE DATABASE "+ databaseName + ";").append("\n");
        fileWriter_SQLDumpFile.append("USE " + databaseName + ";").append("\n");


        // tables create queries
        while(fileReader_DataDictionary.hasNext())
        {
            String columnsString = "";
            String createStmt = "";

            if(firstLine)
            {
                // not needed first line of data dictionary in sql dump
                fileReader_DataDictionary.nextLine();
                firstLine = false;
            }
            else
            {
                lineInDataDictionary = fileReader_DataDictionary.nextLine();
                lineInDataDictionary = lineInDataDictionary.replace(";", "");
                List<String> tableEntity = List.of(lineInDataDictionary.split(Constants.tableColumnSeparator));
                String tableName = tableEntity.get(0);
                List<String> tableColumns = List.of(tableEntity.get(1).split(Constants.columnColumnSeparator));
                //System.out.println(tableColumns);

                if(lineInDataDictionary.contains(Constants.foreignKey))
                {
                    String foreignKeyText = tableColumns.get(tableColumns.size()-1); // FOREIGN_KEY (MainTableName,ColumnName);
                    String[] processingArray = foreignKeyText.split(",");
                    String referencedTableName = processingArray[0].split("[(]")[1];
                    String referencedColumnName = processingArray[1].split("[)]")[0];

                    for(int i =0; i< tableColumns.size()-1; i++)
                    {
                        columnsString += tableColumns.get(i) +',';
                    }

                    columnsString = columnsString.replace(Constants.primarykey, "PRIMARY KEY");

                    String foreignKeyStmt =  "FOREIGN KEY (" + referencedColumnName + ") REFERENCES "+  referencedTableName + "(" + referencedColumnName + ")";
                    columnsString += foreignKeyStmt;

                    createStmt = formCreateTableStatement(tableName, columnsString);

                    fileWriter_SQLDumpFile.write(createStmt + ";");
                    fileWriter_SQLDumpFile.append("\n");
                }
                else
                {
                    //table1	>??<	t1_id INT	>?<	 name VARCHAR	>?<	 mail VARCHAR	>?<	contact VARCHAR	>?<	PRIMARY_KEY (t1_id);

                    for(int i =0; i< tableColumns.size(); i++)
                    {
                        columnsString += tableColumns.get(i);
                        if(i != tableColumns.size()-1)
                            columnsString += ",";
                    }

                    columnsString = columnsString.replace(Constants.primarykey, "PRIMARY KEY");

                    createStmt = formCreateTableStatement(tableName, columnsString);
                    fileWriter_SQLDumpFile.write(createStmt + ";");
                    fileWriter_SQLDumpFile.append("\n");
                }

                // tables insert queries
                String tablePath = Constants.outputFolderPath  + databaseName + "/" + tableName.trim() + ".txt";
                String insertDataStmt = formInsertTableStatement(tableName.trim(), tablePath);
                if(insertDataStmt != null)
                {
                    fileWriter_SQLDumpFile.write(insertDataStmt);
                    fileWriter_SQLDumpFile.append("\n");
                }
            }

        }

        fileReader_DataDictionary.close();
        fileWriter_SQLDumpFile.close();
    }

    public static String formCreateTableStatement(String tableName, String columns){

        //System.out.println(columns);
        String statement = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(" + "%s " + ")", tableName, columns);

        return  statement;

    }

    public static String formInsertTableStatement(String tableName, String path) throws FileNotFoundException {

        File table_file = new File(path);
        Scanner fileReader_TableData = new Scanner(table_file);
        boolean firstLine = true;
        List<String> columns = new ArrayList<>();
        List<List<String>> dataList = new ArrayList<>();
        while(fileReader_TableData.hasNext())
        {
            if(firstLine)
            {
                for(String col : fileReader_TableData.nextLine().split(Constants.columnColumnSeparator))
                {
                    columns.add(col.trim());
                }
                firstLine = false;
            }
            else
            {
                List<String> tableData = List.of(fileReader_TableData.nextLine().split(Constants.columnColumnSeparator));
                dataList.add(tableData);
            }
        }

        fileReader_TableData.close();

        String columnString = String.join(",", columns);

        if(dataList.size() == 0)
            return null;

        List<String> createStmt = prepareStatements(tableName, columnString, dataList);

        return createStmt.get(0);
    }

    private static List<String> prepareStatements(String tableName, String columnNames, List<List<String>> rows) {
        List<String> result = new ArrayList<>();
        rows.forEach(row->{
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<row.size();i++){
                sb.append(row.get(i).trim());
                if(i<row.size()-1){
                    sb.append(",");
                }
            }
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columnNames, sb.toString());
            result.add(sql);
        });
        //System.out.print(result);
        return result;
    }
}
