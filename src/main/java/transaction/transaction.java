package transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import database.DatabaseHandler;

class transaction{
    private final List<String> changedFilesList;
    private List<String> currentQueries;
    private TranactionTable table;
    private String database = "";

    //costructor of the class
    transaction(String user){
        this.changedFilesList =  new ArrayList<String>();
        this.currentQueries = new ArrayList<String>();
        this.table = new TranactionTable(user);
    }
    
    //checks start of the queries and returns true if query statements has right keywords
    private boolean syntaxChecker(String query){
        String[] keywords = query.split("\\s+");

        //check create related queries
        if(keywords[0].equals("create")){
            if(keywords[1].equals("table")){
                return true;
            }
            if(keywords[1].equals("database")){
                return true;
            }
        }

        //check insert into queries
        if(keywords[0].equals("insert") && keywords[1].equals("into")){
            for(String word : keywords){
                if(word.equals("values")){
                    return true;
                }
            }
            return false;
        }

        //check delete related queries
        if(keywords[0].equals("delete") && keywords[1].equals("from")){
            return true;
        }

        //checl the update 
        if(keywords[0].equals("update")){
            return true;
        }

        //LOGGING FOR SYNTAX FAILURE
        return false;
    }

    //syntax validation and parsing the transaction code block
    private List<Boolean> syntaxValidation(List<String> lines){
        List<Boolean> syntaxCheckList = new ArrayList<Boolean>();

        int numberofLines = lines.size();
        //compares the first line with "start transaction;"
        if(lines.get(0).toLowerCase().equals("start transaction;") && (lines.get((numberofLines-1)).toLowerCase().equals("commit;") || lines.get((numberofLines-1)).toLowerCase().equals("rollback;"))){
            for(int i = 1; i < (numberofLines-1); i++){
                String tempQueryString = lines.get(i).toLowerCase();
                if(syntaxChecker(tempQueryString)){
                    syntaxCheckList.add(Boolean.TRUE);
                    System.out.println(syntaxCheckList);
                }else{
                    syntaxCheckList.add(Boolean.FALSE);
                    System.out.println(syntaxCheckList);

                }
                
            }
        }
        return syntaxCheckList;
    }

    List<String> removeLine(List<String> lines, int index){
        lines.remove(index); 
        return lines;
    }

    //reads queries from the file
    public boolean processTransaction(String filepath){
        try {
            Path path = Paths.get(filepath);
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String[] useDatabase = lines.get(0).split("\\s+");
            if(useDatabase[0].toLowerCase().equals("use")){
                database = useDatabase[1].substring(0,(useDatabase[1].length()-1));

                //removes database line from the read block
                lines = removeLine(lines, 0);
            }

            for(Boolean syntaxCheckResponse: syntaxValidation(lines)){
                if(syntaxCheckResponse && !database.equals("")){
                   lines = removeLine(lines, 0);
                   for(String query : lines){
                       query = query.substring(0, (query.length()-1));
                       String[] keywords = query.toLowerCase().split("\\s+");
                       try{
                            if(keywords[0].equals("create")){
                                if(keywords[1].equals("table")){
                                    table.createTable(query, database);
                                }
                                if(keywords[1].equals("database")){
                                    table.createDatabase(query);
                                }  
                            }

                            if(keywords[0].equals("update")){
                                table.updateTable(query, database);
                            }

                            if(keywords[0].equals("insert")){
                                table.insertIntoTabel(query, database);
                            }

                            if(keywords[0].equals("delete")){
                                table.deleteFromTable(query, database);
                            }

                            if(keywords[0].equals("commit")){
                                table.commit();
                            }

                            if(keywords[0].equals("rollback")){
                                table.rollback();
                            }

                       }catch(Exception e){
                            System.out.println(e);
                       }    
                       
                }
            }
            return true;
        }
        }catch(Exception e){
            //LOG - for not able to read the file
                return false;
        }
        return false;
    }

    // `

}