package ui;

import auth.User;
import database.DatabaseHandler;
import datadump.datadumpCreator;
import erd.erdCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            System.out.println("Home page");
            System.out.println("1. Write Queries");
            System.out.println("2. Export");
            System.out.println("3. Data Model");
            System.out.println("4. Analytics");
            System.out.println("5. Transaction");
            System.out.println("5. Quit");
            System.out.print("Enter value of option chosen and press enter to continue: ");
            String line = reader.readLine();
            if(line.equals("6")) {
                return;
            }
            if(line.equals("5")){

            }
            if(line.equals("4")) {
                erdCreator.createERDDiagram("u1", "db1");
            }
            if(line.equals("3")) {
                // TODO: 3/18/2022
            }
            if(line.equals("2")) {
                datadumpCreator.createDataDump("u1", "db1");
            }
            if(line.equals("1")) {
                // TODO: 3/18/2022
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.CreateDatabase("create DATABASE tester");
                databaseHandler.showDatabase();
                databaseHandler.CreateTable("create table kanu (userid varchar(255) FOREIGN KEY REFERENCES anas(userid), username int primary key)","DB1");
                databaseHandler.SelectFromTable("select userid,username from user where userid in (danu)","DB1");
                databaseHandler.CheckUpdate("update user set userid = benny where userid != benny ", "DB1");
                databaseHandler.CheckDelete("delete from user where userid = benny,asd)","DB1");
                databaseHandler.CheckInsert("insert into user values (a,1,c),  (d,2,f)","DB1");

            }
        }
    }


}
