package ui;

import analysis.Queries;
import auth.User;
import database.DatabaseHandler;
import datadump.datadumpCreator;
import erd.erdCreator;
import logmanagement.GeneralLogs;
import logmanagement.QueryLogs;
import support.GlobalData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import transaction.Trans;

public class GUI {
    private BufferedReader reader;
    private User user;

    public GUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        user = new User();
    }

    public void initialize() throws IOException {
        System.out.println("Please choose login or register to continue");
        System.out.println("1. Login");
        System.out.println("2. Registration");
        System.out.print("Enter value of option chosen and press enter to continue: ");
        String line = reader.readLine();
        if (line.equals("1")) {
            if(user.authenticate()) {
                home();
            } else {
                System.out.println("Invalid authentication");
            }
        } else if (line.equals("2")) {
            user.registration();
        }

    }

    public void home() throws IOException {
        while (true) {
            System.out.println("=============================================");
            System.out.println("Home page");
            System.out.println("=============================================");
            System.out.println("1. Write Queries");
            System.out.println("2. Export");
            System.out.println("3. Data Model");
            System.out.println("4. Analytics");
            System.out.println("5. Quit");
            System.out.print("Enter value of option chosen and press enter to continue: ");
            String line = reader.readLine();
            if(line.equals("1")) {
                handleQuery();
            }
            if(line.equals("2")) {
                System.out.print("Please provide database name: ");
                String dbName = reader.readLine();
                datadumpCreator.createDataDump(GlobalData.userId, dbName);
            }
            if(line.equals("3")) {
                System.out.print("Please provide database name: ");
                String dbName = reader.readLine();
                erdCreator.createERDDiagram(GlobalData.userId, dbName);
            }
            if(line.equals("4")) {
                System.out.print("Enter search term: ");
                String analysis = reader.readLine();
                Queries queries = new Queries();
                if(analysis.equals("count queries")) {
                    queries.printCountQuery();
                } else {
                    queries.printCountUpdate();
                }
            }
            if(line.equals("5")) {
                return;
            }
        }
    }

    private void handleQuery() throws IOException {
        DatabaseHandler databaseHandler = new DatabaseHandler();
        GeneralLogs genLogs = new GeneralLogs();
        QueryLogs queryLogs = new QueryLogs();
        System.out.print("Enter query: ");
        String query = reader.readLine();
        query = query.toLowerCase();
        List<String> queries = new ArrayList<String>(Arrays.asList(query.split(";")));

        
        if(queries.size() > 1){
            Trans tx = new Trans();
            tx.processTransaction(queries); 

        } else if (query.contains("select")) {
            long startTime = System.currentTimeMillis();
            databaseHandler.SelectFromTable(query,"DB1");
            long endTime = System.currentTimeMillis();
            genLogs.writeGeneralLogs(startTime,endTime);
            queryLogs.writeQueryLogs(query,"DB1");
        } else if (query.contains("update")) {
            long startTime = System.currentTimeMillis();
            databaseHandler.CheckUpdate(query, "DB1");
            long endTime = System.currentTimeMillis();
            genLogs.writeGeneralLogs(startTime,endTime);
            queryLogs.writeQueryLogs(query,"DB1");
        } else if (query.contains("delete")) {
            long startTime = System.currentTimeMillis();
            databaseHandler.CheckDelete(query,"DB1");
            long endTime = System.currentTimeMillis();
            genLogs.writeGeneralLogs(startTime,endTime);
            queryLogs.writeQueryLogs(query,"DB1");
        } else if (query.contains("insert")) {
            long startTime = System.currentTimeMillis();
            databaseHandler.CheckInsert(query,"DB1");
            long endTime = System.currentTimeMillis();
            genLogs.writeGeneralLogs(startTime,endTime);
            queryLogs.writeQueryLogs(query,"DB1");
        } else if (query.contains("create")) {
            if (query.contains("database")) {
                long startTime = System.currentTimeMillis();
                databaseHandler.CreateDatabase(query);
                long endTime = System.currentTimeMillis();
                genLogs.writeGeneralLogs(startTime,endTime);
                queryLogs.writeQueryLogs(query,"DB1");
            } else if (query.contains("table")) {
                long startTime = System.currentTimeMillis();
                databaseHandler.CreateTable(query,"DB1");
                long endTime = System.currentTimeMillis();
                genLogs.writeGeneralLogs(startTime,endTime);
                queryLogs.writeQueryLogs(query,"DB1");
            }
        }
        System.out.println("Query executed");
    }


}
