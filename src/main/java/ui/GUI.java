package ui;

import auth.User;
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
            System.out.println("5. Quit");
            System.out.print("Enter value of option chosen and press enter to continue: ");
            String line = reader.readLine();
            if(line.equals("5")) {
                return;
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
            }
        }
    }


}
