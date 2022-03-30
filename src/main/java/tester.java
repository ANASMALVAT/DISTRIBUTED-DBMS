//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//
////import java.io.*;
////import java.util.HashMap;
////import java.util.regex.Pattern;
////
//////import java.io.*;
//////import java.util.HashMap;
//////import java.util.regex.Pattern;
//////
//class tester{
//    public static String DbPath = "./DatabaseSystem/Database/";
//    public static final String seprator = "<" + String.valueOf("*") + String.valueOf("*") + ">";
//
//    public static boolean CheckColumnExist(String DbName,String TableName, String ColumnName) throws IOException{
//        String curPath = DbPath + DbName + "/" + TableName +  "/" + "meta.txt";
//        File fr = new File(curPath);
//        BufferedReader bf = new BufferedReader(new FileReader(fr));
//        String columns = bf.readLine();
//        String [] Col = columns.split(seprator);
//        for(int i = 0 ; i < Col.length; i ++){
//            if(Col[i].trim().equals(ColumnName)){
//                return true;
//            }
//        }
//        System.out.println("Column Doesnot Exist !");
//        return false;
//    }
//
//    public static boolean CheckDelete(String query) throws IOException {
//
//        query = query.toLowerCase();
//        String []words = query.split(" ");
//        if(words.length < 3){
//            return false;
//        }
//        if(!words[0].equals("delete") || !words[1].equals("from")){
//            System.out.println("Problem in the keyword update or set");
//            return false;
//        }
//        if(!CheckTableExist(words[2].trim())){
//            System.out.println("Table doesnot exist");
//            return false;
//        }
//        String TableName = words[2].trim();
//
//        if(words.length == 3){
//            TruncateTable(User1DB, words[2]);
//        }
//        else{
//                if(!query.contains("where")){
//                    System.out.println("No where clause");
//                    return false;
//                }
//                else{
//                    String [] Where = query.split("where");
//                    if(Where.length == 1){
//                        System.out.println("Incomplete Where Condition !");
//                        return false;
//                    }
//
//                    String AfterWhere = Where[1];
//                    if(AfterWhere.contains("=") || AfterWhere.contains("!=") ||
//                            AfterWhere.contains("in") || AfterWhere.contains("not in")) {
//                            String[] ColumnValue;
//                            if (AfterWhere.contains("=")) {
//                                ColumnValue = AfterWhere.split("=");
//                            } else if(AfterWhere.contains("in")){
//                                ColumnValue = AfterWhere.split("in");
//                            }
//                            else if(AfterWhere.contains("not in")){
//                                ColumnValue = AfterWhere.split("not in");
//                            }
//                            else{
//                                ColumnValue = AfterWhere.split("!=");
//                            }
//                            HashMap<String, Integer> Tcol = new HashMap<>();
//                            HashMap<String, Integer> UpdateVal = new HashMap<>();
//
//                        String curPath = DbPath + User1DB + "/" + words[1] + "/meta.txt";
//                            File f = new File(curPath);
//                            BufferedReader bf = new BufferedReader(new FileReader(f));
//                            String tableCol = bf.readLine();
//                            String[] tmp = tableCol.split(Pattern.quote(seprator));
//                            for (Integer i = 0; i < tmp.length; i++) {
//                                Tcol.put(tmp[i], i);
//                            }
//                            if (!Tcol.containsKey(ColumnValue[0])) {
//                                System.out.println("Column Doesnot Exist");
//                                return false;
//                            }
//                            ColumnValue[1] = ColumnValue[1].replaceAll("[()]", "");
//                            String[] Columns = ColumnValue[1].split(",");
//                            for(int i = 0; i < Columns.length ; i ++){
//                                UpdateVal.put(Columns[i].trim(), 1);
//                            }
//                            int Notin = 1;
//                            if(AfterWhere.contains("=") || AfterWhere.contains("in")){
//                                Notin = 0;
//                            }
//                            OperationUD(TableName,Columns[0].trim(),UpdateVal,null,1,Notin);
//                    }
//                    else{
//                        System.out.println("Query is out of scope !");
//                        return false;
//                    }
//
//                }
//
//        }
//    }
////        HashMap<Integer,String > UpdateVal;
////
////            HashMap<Integer,String> colValue = new HashMap<>();
////            String []temp = query.split("set");
////            String  AfterSet = temp[1];
////            String[] temp1 = AfterSet.split("where");
////            String col=  temp1[0].trim();
////            String[] Columns = col.split(",");
////            String curPath = DbPath + User1DB +"/" + words[1] + "/meta.txt";
////            File f = new File(curPath);
////            BufferedReader bf = new BufferedReader(new FileReader(f));
////            String tableCol = bf.readLine();
////            String [] tmp = tableCol.split(Pattern.quote(seprator));
////            HashMap<String,Integer> Tcol = new HashMap<>();
////            for(int i  = 0 ; i < tmp.length; i ++){ Tcol.put(tmp[i],i); }
////            for(int i = 0; i < Columns.length; i ++){
////                String []CurCol = Columns[i].split("=");
////                if(Tcol.containsKey(CurCol[0])){
////                    colValue.put(Tcol.get(CurCol[0]) , CurCol[1]);
////                }
////            }
////            if(!query.contains("where")) {
////                return OperationUD(words[1],"","",UpdateVal,0);
////            }
////            else{
////                String [] whereCol = temp1[1].split("=");
////                  return OperationUD(words[1],whereCol[0],whereCol[1],UpdateVal,0);
////            }
////    }
////    public static void main(String args[]){
////        String query= "NSERT INTO sales.promotions (\n" +
////                "    promotion_name,\n" +
////                "    discount,\n" +
////                "    start_date,\n" +
////                "    expired_date\n" +
////                ")\n" +
////                "VALUES\n" +
////                "    (\n" +
////                "        '2019 Summer Promotion',\n" +
////                "        0.15,\n" +
////                "        '20190601',\n" +
////                "        '20190901'\n" +
////                "    ),\n" +
////                "    (\n" +
////                "        '2019 Fall Promotion',\n" +
////                "        0.20,\n" +
////                "        '20191001',\n" +
////                "        '20191101'\n" +
////                "    ),\n" +
////                "    (\n" +
////                "        '2019 Winter Promotion',\n" +
////                "        0.25,\n" +
////                "        '20191201',\n" +
////                "        '20200101'\n" +
////                "    );";
////        String[] temp = query.split("VALUES");
////        System.out.println(temp[1]);
////
////    }
////}
//////
//////
//////
////////import javax.xml.crypto.Data;
////////import java.nio.file.Files;
////////import java.io.*;
////////import java.nio.file.Paths;
////////import java.sql.SQLOutput;
////////import java.util.*;
////////import java.util.regex.*;
////////
////////public class tester {
////////
////////    public static String User1DB = "DB1";
////////    public static String seprator = "<" + String.valueOf("*") + String.valueOf("*") + ">";
////////    public static boolean CheckSpecial(String chk) {
////////        Pattern pattern = Pattern.compile("[^a-zA-Z0-9_]");
////////        Matcher matcher = pattern.matcher(chk);
////////        return matcher.find();
////////    }
////////
////////    public static boolean checkForeign(String ref) throws IOException {
////////
////////        String  [] validator = ref.replaceAll("[\\)\\(]", " ").trim().split(" ");
////////
////////        String tableName = validator[0];
////////
////////        String ColumName = validator[1];
////////
////////        String path = "./DatabaseSystem/Database/DB1/" + tableName;
////////
////////        File TableDir = new File(path);
////////
////////        if (TableDir.exists()) {
////////
////////            path +=  "/meta.txt";
////////
////////            File f = new File(path);
////////
////////            BufferedReader bf = new BufferedReader(new FileReader(f));
////////            String line;
////////            while ((line = bf.readLine()) != null) {
////////
////////                String[] words = line.split(Pattern.quote(seprator));
////////
////////                for (int i = 0; i < words.length; i++) {
////////                    String []tmp = words[i].split(" ");
////////                    if(tmp.length == 2){
////////                        if(tmp[0].equals(ColumName)){
////////                            return true;
////////                        }
////////                    }
////////                    if (words[i].equals(ColumName)) {
////////
////////                        return true;
////////                    }
////////                }
////////
////////            }
////////        }
////////        else{
////////            System.out.println("refrenced table in the foreign key doesnot key");
////////            return false;
////////        }
////////        System.out.println("Referenced Column doesnot exist");
////////        return false;
////////    }
////////        public static void main (String args[]) throws IOException {
////////            HashMap<String, Integer> DataType = new HashMap<>() {
////////                {
////////                    put("int", 1);
////////                    put("varchar(255)", 1);
////////                }
////////            };
////////            HashMap<String, Integer> SQLkeys = new HashMap<>() {
////////                {
////////                    put("primary", 1);
////////                    put("foreign", 1);
////////                }
////////            };
////////            if (User1DB == "None") {
////////                System.out.println("You should first select the Database to create a table");
//////////         return false;
////////            }
////////            String path = "./DatabaseSystem/Database/" + User1DB + "/";
////////            String query = "CREATE TABLE user (  userId IT PRIMARY KEY ,  username VARCHAR(255) foreign key (username) references anas(tablename),  password VARCHAR(255));";
////////            query = query.toLowerCase();
////////            String arr[] = query.split(" ");
////////
//////////          Checking for the create keyword
////////            if (arr[0].equals("create") == false) {
////////                System.out.println("Please Enter Valid Query: Error in " + arr[0] + "  KEYWORD");
////////                //    return false;
////////            }
////////
//////////           Checking for the table keyword
////////            if (!arr[1].equals("table")) {
////////                System.out.println("return false2");
////////                System.out.println("Please Enter Valid Query: Error in " + arr[1] + "KEYWORD");
////////                //        return false;
////////            }
////////
//////////           Checking for the tableName
////////            String TableName = "";
////////            String[] TableSeprate = arr[2].split("\\(");
////////            TableName = TableSeprate[0];
////////            if (CheckSpecial(TableName)) {
////////                System.out.println(arr[2] + " contains special character");
////////            }
////////            int lastC = 0;
////////            for (int i = 0; i < query.length(); i++) {
////////                if (query.charAt(i) == '(') {
////////                    lastC = i;
////////                    break;
////////                }
////////            }
////////
//////////          Checking for the validation of end of the query
////////
////////            String Columns = query.substring(lastC + 1, query.length() - 2);
////////            System.out.println("Columns :" + Columns);
////////            if (!query.substring(query.length() - 2, query.length()).equals(");")) {
////////                System.out.println("Query is not valid");
////////            }
////////
//////////          Checking for the columns of the table
////////
////////            String ColInsert = "";
////////            String DataInsert  = "";
////////            HashMap<String,Integer> Coldup = new HashMap<>();
////////            String[] column = Columns.split(",");
////////
////////            for (String col : column) {
////////
////////                col = col.trim();
////////
////////                String[] curCol = col.split(" ");
////////
////////                if (curCol.length < 2) {
////////
////////                    System.out.println("Invalid Query");
//////////                    return false;
////////                }
////////
////////                for(int j = 0 ; j < curCol.length; j ++){
////////
////////                    System.out.println(curCol[j] + " "  + j);
//////////                  Checking for the name of the column
////////
////////                    if(j == 0 && !SQLkeys.containsKey(curCol[j]) && !DataType.containsKey(curCol[j]) && !(CheckSpecial(curCol[j])) && !Coldup.containsKey(curCol[j])){
////////                        ColInsert += curCol[j];
////////                        Coldup.put(curCol[j] , 1);
////////                    }
////////
//////////                  Checking for the  datatype of the column
////////                    else if(j == 1 && DataType.containsKey(curCol[j])){
////////                        DataInsert += curCol[j];
////////                    }
////////                    else if(j == 1 && !DataType.containsKey(curCol[j])){
////////                        System.out.println("Wrong Datatype entered");
//////////                        return false;
////////                    }
////////
//////////                   checking for the sql keys of the table
////////
////////                    if(j >= 2){
////////
////////                        if(SQLkeys.containsKey(curCol[j])){
////////
////////                            if(curCol[j].equals("primary")){
////////                                if(j + 1 < curCol.length && curCol[j + 1].equals("key")) {
////////                                    ColInsert += " primarykey";
////////                                }
////////                                else{
////////                                    System.out.println("problem in primary key keyword");
//////////                                    return false;
////////                                }
////////                            }
////////
////////                            else if(curCol[j].equals("foreign")) {
////////
////////                                if (curCol.length - j >= 4) {
////////                                    System.out.println(curCol[j + 1]  + "  ***   " + curCol[j + 2] + " ***  ");
////////
////////                                    if (curCol[j + 1].equals("key") &&
////////
////////                                            curCol[j + 3].equals("references")) {
////////
////////                                        System.out.println("in the reference");
////////
////////                                        if (curCol[j + 2].equals("(" + curCol[0] + ")") && checkForeign(curCol[j + 4])) {
////////
////////                                        } else {
////////
////////                                            System.out.println("problem in foreign key");
//////////
//////////                                            return false;
////////                                        }
////////                                    }
////////                                    else{
////////
////////                                        System.out.println("Problem in keywords such as refrences or foreign key");
////////                                    }
////////                                }
////////                                else{
////////
////////                                    System.out.println("foreign key not valid");
////////                                }
////////                            }
////////                            else{
////////                                    System.out.println("problem in query 2");
//////////                                    return false;
////////                            }
////////                        }
////////                        else{
//////////                            System.out.println(curCol[j]);
//////////                            System.out.println("Not a key");
//////////                            return false;
////////                        }
////////                    }
////////                }
////////                ColInsert += seprator;
////////                DataInsert += seprator;
////////            }
////////                        path = path + TableName;
////////                        System.out.println(path);
////////                        File TableDir = new File(path);
////////                        if (TableDir.exists()) {
////////                            System.out.println("Table Already Exist!");
//////////                            return false;
////////                        }
////////                        TableDir.mkdir();
////////                        String datafile = path + "/data.txt";
////////                        String metafile = path + "/meta.txt";
////////                        File MetaFile  = new File(metafile);
////////                        File DataFile  = new File(datafile);
////////                        MetaFile.createNewFile();
////////                        DataFile.createNewFile();
////////                        FileWriter fw  = new FileWriter(metafile);
////////                        System.out.println(ColInsert);
////////                        System.out.println(DataInsert);
////////                        fw.write(ColInsert+ "\n") ;
////////                        fw.write(DataInsert);
////////                        fw.close();
////////        }
////////    }