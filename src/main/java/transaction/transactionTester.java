package transaction;

import database.DatabaseHandler;

class transactionTester{
    public static void main(String[] args) throws Exception {
        transaction tp = new transaction("user1");
        // DatabaseHandler dh =  new DatabaseHandler();
        String filePath = "/Users/home/Documents/DMWA_Project/dmwa-project-team_dpg_17/src/main/java/transaction/queries.txt";
        

        System.out.println(tp.processTransaction(filePath));
        // tp.readQueries(filePath);
        // tt.createDatabase("create database db3");
        // tt.createTable("create table kanu (userid varchar(255), username int primary key)", "DB1");
        // tt.insertIntoTabel("insert into kanu values (Harshit, 1), (Vasu, 5)", "DB1");
        // tt.updateTable("update kanu set userid = benny where userid != Harshit", "DB1");
        // tt.deleteFromTable("delete from kanu where userid = Harshit", "DB1");
        // tt.rollback();
        // tt.commit();
        
    }
}