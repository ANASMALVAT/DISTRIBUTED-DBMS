package transaction;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import org.apache.commons.io.FileUtils;


import database.DatabaseHandler;

class TranactionTable{
    private String user = "";
    private final String databasepath = "./DatabaseSystem/Database/";
    static Locker lock = new Locker();
    Stack<List<String>> stack = new Stack<List<String>>();

    TranactionTable(String user){
        this.user = user;
    }

    //commit
    //database and create Table do not need any changes
    //insert into, delete and update queries - temporary tables needs to be repalced by the permenant one.
    void commit(){
        List<String> tempElement;
        String originaltable;
        String database;
        String operation;

        try{
            for(int i=stack.size(); i > 0 ; i--){
                tempElement = stack.pop();

                originaltable = tempElement.get(0);
                database = tempElement.get(1);
                operation = tempElement.get(2);

                if(operation.equals("insertInto")){
                    DoSwap(originaltable, database);
                }

                if(operation.equals("deleteFrom")){
                    DoSwap(originaltable, database);
                }

                if(operation.equals("updateTable")){
                    DoSwap(originaltable, database);
                }
            }

            //LOG - TRANSACTION IS ROLLED BACK
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void DoSwap(String originaltable, String database) throws IOException {
        File originalFile = new File(databasepath+ database +"/"+ originaltable +"/");
        File tempFile = new File(databasepath+ database +"/"+ originaltable +"temp/");
        if(tempFile.exists()){

            File[] directoryListing = originalFile.listFiles();
            for(File file: directoryListing)
                if (!file.isDirectory())
                {
                    System.out.println(file.getName());
                    file.delete();
                }
            FileUtils.copyDirectory(tempFile, originalFile);
            System.gc();

            Files.delete(Path.of(databasepath + database + "/" + originaltable + "temp/data.txt"));
            Files.delete(Path.of(databasepath + database + "/" + originaltable + "temp/meta.txt"));
            Files.delete(Path.of(databasepath + database + "/" + originaltable + "temp/"));

        }else{
            throw new FileNotFoundException();
        }
    }

    //rollback
    //database and create Table files gets deleted
    //insert into, delete and update queries - temporary tables needs to be deleted.
    void rollback(){
        List<String> tempElement;
        String originaltable;
        String database;
        String operation;

        try{
            for(int i=stack.size(); i > 0 ; i--){
                tempElement = stack.pop();

                originaltable = tempElement.get(0);
                database = tempElement.get(1);
                operation = tempElement.get(2);

                if(operation.equals("insertInto")){
                    TempRollbackDelete(originaltable, database);
                }

                if(operation.equals("deleteFrom")){
                    TempRollbackDelete(originaltable, database);
                }

                if(operation.equals("updateTable")){
                    TempRollbackDelete(originaltable, database);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }

    }

    private void TempRollbackDelete(String originaltable, String database) throws IOException {
        System.gc();
        Files.delete(Path.of(databasepath + database + "/" + originaltable + "temp/data.txt"));
        Files.delete(Path.of(databasepath + database + "/" + originaltable + "temp/meta.txt"));
        Files.delete(Path.of(databasepath + database + "/" + originaltable + "temp/"));
    }

    Boolean createDatabase(String query){
        try{
            if(DatabaseHandler.CreateDatabase(query)){
                String[] keywords = query.toLowerCase().split("\\s+");
                List<String> tempList  = Arrays.asList(keywords[2], keywords[2],"creatDatabase");
                stack.push(tempList);
                return true;
            }
            return false;
        }catch(Exception e){
            rollback();
        }
        return false;
    }

    Boolean createTable(String query, String database){
        try{
            if(DatabaseHandler.CreateTable(query, database)){
                String[] keywords = query.toLowerCase().split("\\s+");
                List<String> tempList  = Arrays.asList(keywords[2], database, "creatTable");
                stack.push(tempList);
                return true;
            }
            return false;
        }catch(Exception e){
            return false;
        }
    }

    Integer createDirectory(String writingPath){
        try{
            File theDir = new File(writingPath);
            if (!theDir.exists()){
                theDir.mkdirs();

                File dataTxt = new File(writingPath+"/data.txt");
                dataTxt.createNewFile();

                File metaTxt = new File(writingPath+"/meta.txt");
                metaTxt.createNewFile();
            }else{
                return 0;
            }
            return 1;
        }catch(Exception e){
            System.out.println(e);
            return -1;
        }

    }

    Boolean createDumpTable(String originalTableName, String database){
        String readingPath = "./DatabaseSystem/Database/"+database+"/"+originalTableName;
        String writingPath = "./DatabaseSystem/Database/"+database+"/"+originalTableName+"temp";

        int exist = createDirectory(writingPath);

        if(exist == 1){
            try (BufferedReader br = new BufferedReader(new FileReader(readingPath+"/data.txt"))) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(writingPath+"/data.txt"))) {
                    String text = null;
                    while ((text = br.readLine()) != null) {
                        System.out.println(text);
                        bw.write(text);
                        bw.newLine();
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println(e);

            } catch (IOException e) {
                System.out.println(e);
            }

            try (BufferedReader br = new BufferedReader(new FileReader(readingPath+"/meta.txt"))) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(writingPath+"/meta.txt"))) {
                    String text = null;
                    while ((text = br.readLine()) != null) {
                        System.out.println(text);
                        bw.write(text);
                        bw.newLine();
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println(e);
                return false;
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }

            return true;
        }else if(exist == 0){
            return true;
        }
        return false;
    }

    Boolean insertIntoTabel(String query, String database){
        String orginalTableName = "";
        try{
            String[] keywords = query.toLowerCase().split("\\s+");
            orginalTableName = keywords[2];
            if(lock.setLock(user, database, orginalTableName)){
                System.out.println("Locked");
                if(createDumpTable(orginalTableName, database)){
                    keywords[2] = keywords[2]+"temp";
                    String tempQuery = String.join(" ", keywords);
                    if(DatabaseHandler.CheckInsert(tempQuery , database)){
                        List<String> tempList  = Arrays.asList(orginalTableName, database, "insertInto");
                        stack.push(tempList);
                    }
                }else{
                    return false;
                }
                lock.releaseLock(orginalTableName);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    Boolean deleteFromTable(String query, String database){
        String orginalTableName = "";
        try{
            String[] keywords = query.toLowerCase().split("\\s+");
            orginalTableName = keywords[2];
            if(lock.setLock(user, database, orginalTableName)){
                System.out.println("Locked");
                if(createDumpTable(orginalTableName, database)){
                    keywords[2] = keywords[2]+"temp";
                    String tempQuery = String.join(" ", keywords);
                    if(DatabaseHandler.CheckDelete(tempQuery , database)){
                        List<String> tempList  = Arrays.asList(orginalTableName, database, "deleteFrom");
                        stack.push(tempList);
                    }
                }else{
                    return false;
                }
                lock.releaseLock(orginalTableName);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    Boolean updateTable(String query, String database){
        String orginalTableName = "";
        try{
            String[] keywords = query.toLowerCase().split("\\s+");
            orginalTableName = keywords[1];
            if(lock.setLock(user, database, orginalTableName)){
                //System.out.println("Locked");
                if(createDumpTable(orginalTableName, database)){
                    keywords[1] = keywords[1]+"temp";
                    String tempQuery = String.join(" ", keywords);
                    if(DatabaseHandler.CheckUpdate(tempQuery , database)){
                        List<String> tempList  = Arrays.asList(orginalTableName, database, "updateTable");
                        stack.push(tempList);
                    }
                }
                lock.releaseLock(orginalTableName);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

}