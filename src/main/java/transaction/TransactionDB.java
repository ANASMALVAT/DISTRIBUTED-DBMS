package transaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import database.DatabaseHandler;
import support.GlobalData;

public class TransactionDB{
    private final List<String> changedFilesList;
    private List<String> currentQueries;
    private TranactionTable table;
    private String database = "";

    //costructor of the class
    public TransactionDB(){
        this.changedFilesList =  new ArrayList<String>();
        this.currentQueries = new ArrayList<String>();
        this.table = new TranactionTable(GlobalData.userId);
    }
    
    //checks start of the queries and returns true if query statements has right keywords
    private boolean syntaxChecker(String query){
        query = query.trim();

        //check create related queries
        if(query.contains("create")){
            if(query.contains("table")){
                return true;
            }
            if(query.contains("database")){
                return true;
            }
        }

        //check insert into queries
        if(query.contains("insert") && query.contains("into")){
        
                if(query.contains("values")){
                    return true;
                }

            return false;
        }

        //check delete related queries
        if(query.contains("delete") && query.contains("from")){
            return true;
        }

        //checl the update 
        if(query.contains("update")){
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
        if(lines.get(0).toLowerCase().equals("start transaction") && (lines.get((numberofLines-1)).toLowerCase().equals("commit") || lines.get((numberofLines-1)).toLowerCase().equals("rollback"))){
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
    public boolean processTransaction(List<String> lines){
        // System.out.println(lines);
        database = lines.get(0).substring(4,(lines.get(0).length()));
        try {
            String[] useDatabase = lines.get(0).split("\\s+");

            if(useDatabase[0].toLowerCase().equals("use")){
                //removes database line from the read block
                lines = removeLine(lines, 0);
            }

            for(Boolean syntaxCheckResponse: syntaxValidation(lines)){
                if(syntaxCheckResponse && !database.equals("")){
                   lines = removeLine(lines, 0);
                   for(String query : lines){
                       query = query.trim();
                       String[] keywords = query.toLowerCase().split("\\s+");
                       try{
                            if(keywords[0].equals("create")){
                                if(keywords[1].equals("table")){
                                    table.createTable(query, database);
                                    //LOG - create table sucessfull
                                }
                                if(keywords[1].equals("database")){
                                    table.createDatabase(query);
                                    //LOG - create database sucessfull
                                }  
                            }

                            if(keywords[0].equals("update")){
                                table.updateTable(query, database);
                                //LOG - updated value done
                            }

                            if(keywords[0].equals("insert")){
                                table.insertIntoTabel(query, database);
                                //LOG - insert into done
                            }

                            if(keywords[0].equals("delete")){
                                table.deleteFromTable(query, database);
                                //LOG - delete from a table is done
                            }

                            if(keywords[0].equals("commit")){
                                table.commit();
                                //LOG - transaction commited
                            }

                            if(keywords[0].equals("rollback")){
                                table.rollback();
                                //LOG - transaction rolled back
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