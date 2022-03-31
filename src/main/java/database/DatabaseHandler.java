package database;

import java.io.*;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.regex.*;


public class DatabaseHandler {

    public static final String seprator = "<**>";
    public static  String User1DB ="db1";
    public static  String User2DB ="None";

    public static String DbPath = "./DatabaseSystem/Database/";

    public static boolean CheckSpecial(String chk) {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9_]");
            Matcher matcher = pattern.matcher(chk);
            return matcher.find();
    }

    public static boolean isNumeric(String string) {
        int intValue;

        System.out.println(String.format("Parsing string: \"%s\"", string));

        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    public static boolean checkForeign(String ref,String DbName) throws IOException {

            String  [] validator = ref.replaceAll("[\\)\\(]", " ").trim().split(" ");
            String tableName = validator[0].trim();
            String ColumName = validator[1].trim();
            String path = DbPath + DbName + "/" + tableName;
            File TableDir = new File(path);
            if (TableDir.exists()) {
                path +=  "/meta.txt";
                File f = new File(path);
                BufferedReader bf = new BufferedReader(new FileReader(f));
                String line;
                while ((line = bf.readLine()) != null) {
                    String[] words = line.split(Pattern.quote(seprator));
                    for (int i = 0; i < words.length; i++) {
                        String []tmp = words[i].split(" ");
                            if(tmp[0].trim().equals(ColumName)){
                                return true;
                            }
                    }
                }
            }
            else{
                System.out.println("refrenced table in the foreign key doesnot key");
                return false;
            }
            System.out.println("Referenced Column doesnot exist");
            return false;
        }

    public static boolean CreateTable(String query,String DbName) throws IOException {

        HashMap<String, Integer> DataType = new HashMap<>() {
            {
                put("int", 1);
                put("varchar(255)", 1);
            }
        };
        HashMap<String, Integer> SQLkeys = new HashMap<>() {
            {
                put("primary", 1);
                put("foreign", 1);
            }
        };
            if (DbName == "None") {
            System.out.println("You should first select the Database to create a table");
         return false;
        }
        String path = DbPath + DbName + "/";
        query = query.toLowerCase();
        String arr[] = query.split(" ");

//          Checking for the create keyword
            if (!arr[0].equals("create")) {
            System.out.println("Please Enter Valid Query: Error in " + arr[0] + "  KEYWORD");
                return false;
        }

//           Checking for the table keyword
            if (!arr[1].equals("table")) {
            System.out.println("Please Enter Valid Query: Error in " + arr[1] + "KEYWORD");
                    return false;
        }

        //           Checking for the tableName
        String TableName = "";
        String[] TableSeprate = arr[2].split("\\(");
        TableName = TableSeprate[0];
            if (CheckSpecial(TableName)) {
            System.out.println(arr[2] + " contains special character");
        }
        int lastC = 0;
            for (int i = 0; i < query.length(); i++) {
            if (query.charAt(i) == '(') {
                lastC = i;
                break;
            }
        }

//          Checking for the validation of end of the query
        String Columns = query.substring(lastC + 1, query.length() - 1);

            if (!query.substring(query.length() - 1, query.length()).equals(")")) {
            System.out.println("Query is not valid");
        }

//          Checking for the columns of the table
        String ColInsert = "";
        String DataInsert  = "";
        HashMap<String,Integer> Coldup = new HashMap<>();
        String[] column = Columns.split(",");
        for (String col : column) {
            col = col.trim();
            String[] curCol = col.split(" ");
            if (curCol.length < 2) {
                System.out.println("Invalid Query");
                    return false;
                }
            for(int j = 0 ; j < curCol.length; j ++){
//                  Checking for the name of the column
                if(j == 0 && !SQLkeys.containsKey(curCol[j]) && !DataType.containsKey(curCol[j]) && !(CheckSpecial(curCol[j])) && !Coldup.containsKey(curCol[j])){
                    ColInsert += curCol[j];
                    Coldup.put(curCol[j] , 1);
                }

//                  Checking for the  datatype of the column
                else if(j == 1 && DataType.containsKey(curCol[j])){
                    DataInsert += curCol[j];
                }
                else if(j == 1 && !DataType.containsKey(curCol[j])){
                    System.out.println("Wrong Datatype entered");
                        return false;
                }

//                   checking for the sql keys of the table
                if(j >= 2){
                    System.out.println(curCol[j]);
                    if(SQLkeys.containsKey(curCol[j].trim())){
                        if(curCol[j].trim().equals("primary")){
                            if(j + 1 < curCol.length && curCol[j + 1].equals("key")) {
                                ColInsert += " primarykey";
                                j++;
                            }
                            else{
                                System.out.println("problem in primary key keyword");
                                    return false;
                            }
                        }
                        else if(curCol[j].equals("foreign")) {
                            if (curCol.length - j >= 4) {
                                if (curCol[j + 1].equals("key") &&
                                        curCol[j + 2].equals("references")) {

                                    if (checkForeign(curCol[j + 3],DbName)) {

//---------------------------------------------------------------------------------------------------------------------------------------
                                        System.out.println(TableName + "(" + curCol[0] + ")"  + "-->" +  curCol[j + 3]);



                                    } else {
                                        System.out.println("problem in foreign key");
                                            return false;
                                    }
                                }
                                else{
                                    System.out.println("Problem in keywords such as refrences or foreign key");
                                    return false;
                                }
                                j+= 4;
                            }
                            else{
                                System.out.println("foreign key not valid");
                                return false;
                            }
                        }
                        else{
                            System.out.println("problem in query 2");
                                    return false;
                        }
                    }
                    else{
                            System.out.println("Not a key");
                            return false;
                    }
                }
            }
            ColInsert += seprator;
            DataInsert += seprator;
        }
        path = path + TableName;
                        System.out.println(path);
        File TableDir = new File(path);
          if (TableDir.exists()) {
            System.out.println("Table Already Exist!");
                            return false;
        }
        TableDir.mkdir();
        String datafile = path + "/data.txt";
        String metafile = path + "/meta.txt";
        File MetaFile  = new File(metafile);
        File DataFile  = new File(datafile);
        FileWriter fw = new FileWriter(MetaFile);
                        MetaFile.createNewFile();
                        DataFile.createNewFile();
                        System.out.println(ColInsert);
                        System.out.println(DataInsert);
                        fw.write(ColInsert+ "\n") ;
                        fw.write(DataInsert);
                        fw.close();
                        return true;
    }
    public static  void showDatabase(){

        String path = DbPath;
        File f = new File(path);
        String[] DataBase = f.list();
        for (int i = 0; i < DataBase.length; i++) {
                System.out.println(DataBase[i]);
            }
        }

        public void useDatabase(String  input){
            String[] words = input.split(" ");
            if(words.length > 2){
                InvalidQuery();
                return;
            }
            else{
                String dbName= words[1];
                String path = "";
                path += dbName;
                File file = new File(path);
                if(file.exists()){
                       User1DB = dbName;
                    System.out.println("Using DataBase :" + dbName);
                }else{

                }

            }
        }

    public static boolean CreateDatabase(String query){

        query = query.toLowerCase();
        String [] words = query.split(" ");
        if(!words[0].equals("create") && !words[1].equals("database") || words.length != 3){
            return false;
        }
        String path = DbPath + words[2].trim();
        File f = new File(path);
        if(f.exists()){
             System.out.println("Database Already Exist");
             return false;
         }
        f.mkdir();
        return true;
    }

        public static boolean CheckTableExist(String DbName,String tableName){

        String Cpath = DbPath + DbName + "/" + tableName;
        File f = new File(Cpath);
            if(f.exists()){
                return true;
            }
            return false;
        }
        public static boolean insertTable(String query){

            if(User1DB == "None"){
                System.out.println("The database is not selected");
                return false;
            }
            query = query.toLowerCase();
            String []cols = query.split(" ");
            if(cols.length < 4){
                return false;
            }

            if(!cols[0].equals("insert") || !cols[1].equals("into") || !CheckTableExist(cols[2],User1DB) || !cols[3].equals("values")){
                return false;
            }
            int index = query.indexOf("values");
            query = query.substring(index + 6,query.length());
            return false;
    }

    public static boolean OperationUD(String DbName ,String tableName, String ColumnName, HashMap<String,Integer> oldVal, HashMap<Integer,String> updateVal , int flag, int NotIn) throws IOException {

            String curPath = DbPath + DbName + "/" + tableName;
            String metaPath = curPath + "/meta.txt";
            String dataPath = curPath + "/data.txt";
            File fm = new File(metaPath);
            BufferedReader bm = new BufferedReader(new FileReader(fm));
            String line = bm.readLine();
            String Col[] = line.split(Pattern.quote(seprator));
            int IsColumnExist = -1;
            for(int i = 0; i < Col.length ; i++){
                String[] Colchk = Col[i].split(" ");
                if(Colchk.length == 2){
                    if(Colchk[i].equals(ColumnName)){
                        IsColumnExist = i;
                        break;
                    }
                    else{
                        continue;
                    }
                }
                if(Col[i].equals(ColumnName)){
                    IsColumnExist = i;
                    break;
                }
            }
            if(IsColumnExist == -1 && ColumnName.length() > 0){
                System.out.println("Column Doesnot Exist!");
                return false;
            }
            File fr = new File(dataPath);
            BufferedReader br = new BufferedReader(new FileReader(fr));
            File ft = new File(curPath + "/temp.txt");
            FileWriter fw  = new FileWriter(ft);
            while((line = br.readLine()) != null){
                String words[] = line.split(Pattern.quote(seprator));
                if(flag == 0){
                    for(int i = 0; i < words.length ; i ++){
                        if( ColumnName.length() == 0 || oldVal.containsKey(words[IsColumnExist].trim())) {
                            if(NotIn == 0) {
                                if (updateVal.containsKey(i)) {
                                    words[i] = updateVal.get(i);
                                }
                            }
                        }
                        else{
                            if(NotIn == 1){
                                if (updateVal.containsKey(i)) {
                                    words[i] = updateVal.get(i);
                                }
                            }
                        }
                    }
                    String temp = String.join(seprator,words);
                    fw.write(temp +"\n");

                }

                else if(flag == 1){
                    if(IsColumnExist != -1 && !oldVal.containsKey(words[IsColumnExist].trim())){
                        if(NotIn == 0) {
                            String temp = String.join(seprator, words);
                            fw.write(temp + "\n");
                        }
                    }
                    else{
                        if(NotIn == 1){
                            String temp = String.join(seprator, words);
                            fw.write(temp + "\n");
                        }
                    }
                }
            }
            fw.close();
            br.close();
            fr.delete();
            ft.renameTo(new File(dataPath));
            return true;
        }


    public static boolean CheckUpdate(String query,String DbName) throws IOException {

            query = query.toLowerCase();
            String []words = query.split(" ");
            if(words.length < 4){
                return false;
            }
            if(!words[0].equals("update") || !words[2].equals("set")){
                System.out.println("Problem in the keyword update or set");
                return false;
            }

            if(!CheckTableExist(DbName,words[1])){
                System.out.println("Table does not exist " + words[1]);
                return false;
            }

            HashMap<Integer,String > UpdateVal = new HashMap<>();
            HashMap<String,Integer> oldVal = new HashMap<>();
            HashMap<String,Integer> Tcol = new HashMap<>();
            int Notin = 0;

            String []temp = query.split("set");
            String  AfterSet = temp[1];
            String[] temp1 = AfterSet.split("where");
            if(temp1.length < 2){
                System.out.println("Empty Where condition!");
                return false;
            }

            String col =  temp1[0].trim();
            if(col.length() == 0 || col.isEmpty()){
                System.out.println(" IN VALID QUERY!");
                return false;
            }


            String curPath = DbPath + DbName +"/" + words[1] + "/meta.txt";
            File f = new File(curPath);
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String tableCol = bf.readLine();
            String [] tmp = tableCol.split(Pattern.quote(seprator));

            for(Integer i  = 0 ; i < tmp.length; i ++){
                if(tmp[i].contains(" ")){
                    String[] tmp1 = tmp[i].split(" ");
                    Tcol.put(tmp1[0],i);
                    continue;
                }
                Tcol.put(tmp[i],i);
            }
            String[] Columns = col.split(",");
            if(Columns.length == 0) { Columns[0] = col.trim(); }
            if(Columns.length % 2 != 0 && Columns.length != 1){
                System.out.println("Not valid where condition !");
                return false;
            }
            for(int i = 0; i < Columns.length; i ++){
                String []CurCol = Columns[i].trim().split("=");
                CurCol[0] = CurCol[0].trim();
                CurCol[1] = CurCol[1].trim();
                if(Tcol.containsKey(CurCol[0])){
                    UpdateVal.put(Tcol.get(CurCol[0]) , CurCol[1]);
                }
                else{
                    System.out.println("Column " + CurCol[0] + " Does not Exist!" );
                    return false;
                }
            }
            
            if(!query.contains("where")) {
                return OperationUD(DbName,words[1],"",new HashMap<>(),UpdateVal,0,1);
            }
            else{
                String[] whereCol = new String[0];
                if(temp1[1].contains("=") || temp1[1].contains("!=") || temp1[1].contains("in") || temp1[1].contains("not in")) {
                   if((temp1[1].contains("="))) { whereCol = temp1[1].split("=");}
                    if((temp1[1].contains("!="))) { whereCol = temp1[1].split("!="); Notin = 1;}
                    if((temp1[1].contains("in"))) { whereCol = temp1[1].split("in");}
                    if((temp1[1].contains("not in"))) { whereCol = temp1[1].split("not in"); Notin = 1;}

                }
                else{
                    System.out.println("Query is not valid");
                    return false;
                }
                if(whereCol.length != 2){
                    System.out.println("Problem in the after WHERE condition !");
                    return false;
                }
                if(whereCol[1].contains("(")  || whereCol[1].contains(")")){
                    if(!whereCol[1].contains(")") || !whereCol[1].contains("(")){
                        System.out.println("Not a valid query!");
                        return false;
                    }
                }
                whereCol[1] = whereCol[1].replaceAll("[()]","");
                String  []RealCol = whereCol[1].split(",");
                for(int i = 0; i  < RealCol.length ; i ++){
                    oldVal.put(RealCol[i].trim(),1);
                }
                return OperationUD(DbName,words[1],whereCol[0].trim(),oldVal,UpdateVal,0,Notin);
            }
    }

    public static boolean CheckDelete(String query,String DbName) throws IOException {

            query = query.toLowerCase();
            String []words = query.split(" ");
            if(words.length < 3){
                return false;
            }
            if(!words[0].equals("delete") || !words[1].equals("from")){
                System.out.println("Problem in the keyword update or set");
                return false;
            }
            if(!CheckTableExist(DbName,words[2].trim())){
                System.out.println("Table doesnot exist");
                return false;
            }

            String TableName = words[2].trim();

            if(words.length == 3){
                OperationUD(DbName,words[2].trim(),"",null,null,1,0);
                    return false;
            }
            else{
                if(!query.contains("where")){
                    System.out.println("No where clause");
                    return false;
                }
                else{
                    String [] Where = query.split("where");
                    if(Where.length == 1){
                        System.out.println("Incomplete Where Condition !");
                        return false;
                    }

                    String AfterWhere = Where[1];
                    int Notin = 1;
                    if(AfterWhere.contains("=") || AfterWhere.contains("!=") ||
                            AfterWhere.contains("in") || AfterWhere.contains("not in")) {

                        String[] ColumnValue;
                        if(AfterWhere.contains("!=")){
                            ColumnValue = AfterWhere.split("!=");

                        }
                        else if (AfterWhere.contains("=")) {
                            ColumnValue = AfterWhere.split("=");
                            Notin = 0;
                        }
                        else if(AfterWhere.contains("not in")){
                            ColumnValue = AfterWhere.split("not in");
                        }
                        else{
                            ColumnValue = AfterWhere.split("in");
                            Notin = 0;

                        }
                        if(ColumnValue.length == 1 || ColumnValue[1].trim().equals("")){
                            System.out.println("Invalid Query After Where Clause!");
                            return false;
                        }
                        HashMap<String, Integer> Tcol = new HashMap<>();
                        HashMap<String, Integer> UpdateVal = new HashMap<>();

                        String curPath = DbPath + DbName + "/" + words[2] + "/meta.txt";
                        File f = new File(curPath);
                        BufferedReader bf = new BufferedReader(new FileReader(f));
                        String tableCol = bf.readLine();
                        String[] tmp = tableCol.split(Pattern.quote(seprator));
                        for (Integer i = 0; i < tmp.length; i++) {
                            String [] tmp1 = tmp[i].split(" ");
                            Tcol.put(tmp1[0].trim(), i);
                        }
                        String []TempCol = ColumnValue[0].trim().split(" ");

                        if (!Tcol.containsKey(TempCol[0].trim())) {
                            System.out.println("Column Doesnot Exist");
                            return false;
                        }
                        if(ColumnValue[1].contains("(") || ColumnValue[1].contains(")")){
                            if(!ColumnValue[1].contains("(") || !ColumnValue[1].contains(")")){
                                System.out.println("Not a Valid Query!");
                                return false;
                            }
                        }
                        ColumnValue[1] = ColumnValue[1].replaceAll("[()]", "");
                        String[] Columns = ColumnValue[1].split(",");
                        for(int i = 0; i < Columns.length ; i ++){
                            UpdateVal.put(Columns[i].trim(), 1);
                        }
                        OperationUD(DbName,TableName,TempCol[0].trim(),UpdateVal,null,1,Notin);
                        System.out.println("Values Got Deleted  where column name = " + TempCol[0]  + " and given value");
                    }
                    else{
                        System.out.println("Query is out of scope !");
                        return false;
                    }
                }
            }
            return true;
        }

    public static void fetch(String DB,String TableName, HashMap<Integer,Integer> SelectColumns,String columnName,HashMap<String,Integer> ColVal , int flag, int Notin) throws IOException {
            String met = "/meta.txt";
            String dat = "/data.txt";
            String curPath = DbPath + DB + "/" + TableName;
            File f = new File(curPath + met);
            BufferedReader bf = new BufferedReader(new FileReader(f));
            String Col = bf.readLine();
            String []Columns = Col.split(Pattern.quote(seprator));
            int index = -1;
            for(int i = 0; i < Columns.length;i ++){
                String []coltemp = Columns[i].split(" ");
                if(coltemp[0].trim().equals(columnName.trim())){
                    index = i;
                }
            }
            System.out.println(SelectColumns);
            File f1 = new File(curPath + dat);
            BufferedReader bf1 = new BufferedReader(new FileReader(f1));
            String line;
            while((line = bf1.readLine()) != null) {
                String[] values = line.split(Pattern.quote(seprator));
                if((columnName.equals("") || ColVal.containsKey(values[index].trim()))) {
                    if(Notin == 1){
                        continue;
                    }
                    for (int i = 0; i < values.length; i++) {
                        if (flag == 1 || SelectColumns.containsKey(i)) {
                            System.out.print(values[i] + " ");
                        }
                    }
                    System.out.println();
                }
                else{
                    if (Notin == 1) {
                    for (int i = 0; i < values.length; i++) {
                            if (flag == 1 || SelectColumns.containsKey(i)) {
                                System.out.print(values[i] + " ");
                                }
                        }
                        System.out.println();
                    }
                }
            }
            bf1.close();
            return;
        }

public static HashMap<Integer,Integer> SelectedColumns(String Db , String table ,String query) throws IOException
{
    HashMap<Integer,Integer> mp1= new HashMap<>();
    String curPath = DbPath + Db + "/" + table + "/meta.txt";
    File f = new File(curPath);
    BufferedReader bf = new BufferedReader(new FileReader(f));
    String line = bf.readLine();
    String []Columns = line.split(Pattern.quote(seprator));
    String [] queryCols = query.split(",");
    int cnt  = query.length() - query.replace(",","").length();
    if(queryCols.length <= cnt){
        System.out.println("Columns are missing!");
        return null;
    }
    HashMap<String,Integer> AvaiableCols = new HashMap<>();
    for(int  i = 0  ; i < Columns.length ; i ++){
        String []tmp = Columns[i].split(" ");
        AvaiableCols.put(tmp[0].trim(),i);
    }

    for(int i = 0 ; i < queryCols.length ; i ++){
        if(AvaiableCols.containsKey(queryCols[i].trim())){
            mp1.put(AvaiableCols.get(queryCols[i].trim()),1);
        }
        else{
            System.out.println("Column Doesnot Exist in table!");
            return null;
        }
    }
    return mp1;
}

public static String CheckColumnExist(String DbName, String TableName, String query) throws IOException {

        String[] AfterColumns = new String[0];
        if (query.contains("=") || query.contains("in") || query.contains("!=") || query.contains("not in")) {
            if (query.contains("=")) {
                AfterColumns = query.split("=");
            } else if (query.contains("!=")) {
                AfterColumns = query.split("!=");
            } else if (query.contains("in")) {
                AfterColumns = query.split("in");
            } else if (query.contains("not in")) {
                AfterColumns = query.split("not in");
            }
            if(AfterColumns.length == 1){
                System.out.println("Not a Valid query!");
                return "False";
            }
        }
        else{
            System.out.println("Not a Valid Query !");
            return "False";
        }
        String[] CurColumn = AfterColumns[0].trim().split(" ");
        String ColumnName = CurColumn[0].trim();
        String curPath = DbPath + DbName + "/" + TableName + "/meta.txt";
        File f = new File(curPath);
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String cols = bf.readLine();
        String[] columns = cols.split(Pattern.quote(seprator));
        for(int  i = 0;  i < columns.length ; i ++){
            if(columns[i].contains(" ")) {
               String[] coltemp = columns[i].split(" ");
               if(coltemp[0].trim().equals(ColumnName.trim())){
                    return ColumnName.trim();
                }
            }
            else if(ColumnName.trim().equals(AfterColumns[i].trim())){
                return  ColumnName.trim();
            }
        }
        System.out.println("Column Does not Exist!");
        return "False";
    }

public static HashMap<String,Integer> SendColValues(String query) throws IOException {

        HashMap<String, Integer> val = new HashMap<>();
        if (query.contains("=") || query.contains("in") || query.contains("!=") || query.contains("not in")) {
            String[] Columns = new String[0];
            if (query.contains("=")) {
                Columns = query.split("=");
            } else if (query.contains("!=")) {
                Columns = query.split("!=");
            } else if (query.contains("in")) {
                Columns = query.split("in");
            } else if (query.contains("not in")) {
                Columns = query.split("not in");
            }
            if(Columns.length == 1 || Columns[1].trim().equals("")){
                return null;
            }
            if(Columns[1].contains("(") || Columns[1].contains(")")){
                if(!Columns[1].contains(")") || !Columns[1].contains("(")){
                    System.out.println("Invalid Query!");
                    return null;
                }
            }
            Columns[1] = Columns[1].trim();
            if(Columns[1].contains("(")){
                Columns[1] = Columns[1].substring(1,Columns[1].length() - 1);
            }
            String[] values = Columns[1].split(",");
            int count = Columns[1].length() - Columns[1].replace(",", "").length();
            if(values.length <= count){
                return null;
            }
            if(values.length == 0){
                return null;
            }
            for(int i = 0 ; i < values.length; i++){
                String temp = values[i].trim();
                if(temp == ""){
                    return  null;
                }
                val.put(values[i].trim(),1);
            }
            return val;
        }
        else{
            System.out.println("Either Query is out of scope or Query is invalid!");
            return null;
        }
    }

    public static boolean SelectFromTable(String query,String CurdbName) throws IOException {
        if(User1DB == "None"){
            return false;
        }
        query = query.toLowerCase();
        query = query.trim();
        String []words  =query.split(" ");
        if(!words[0].equals("select") || !query.contains("from") || words.length < 4){
            System.out.println("Invalid query");
            return false;
        }
         if(words[1].equals("*")) {
             if (!words[2].equals("from")) {
                 System.out.println("Query is invalid: Expecting FROM after *");
                 return false;
             } else {
                 if (!CheckTableExist(CurdbName, words[3])) {
                     System.out.println("Table Doesnot Exist!");
                 }
                 if (words.length == 4) {
                     fetch(CurdbName, words[3], null, "", null, 1,0);
                 }
                 else {
                         if (!words[4].equals("where")) {
                             System.out.println("Expecting WHERE CLAUSE!");
                             return false;
                         }
                         String[] AfterWhere = query.split("where");
                         if(AfterWhere.length == 1){
                             System.out.println("No Column After where clause!");
                             return false;
                         }
                     String columnName = CheckColumnExist(CurdbName, words[3].trim(), AfterWhere[1]);
                     if(columnName.equals("False")){
                         return false;
                     }

                     var ColumnValues = SendColValues(AfterWhere[1]);
                     if(ColumnValues == null){
                         System.out.println("Column Values is not mentioend!");
                         return false;
                     }
                     if (columnName.equals("False")) {
                             System.out.println("Column Doesnot Exist!");
                             return false;
                         }
                     int Notin = 0;
                     if(query.contains("not in") || query.contains("!=")){
                         Notin = 1;
                     }
                     fetch(CurdbName, words[3], null, columnName, ColumnValues, 1,Notin);
                     }
                 }
             }
         else{
                 String  [] beforeFrom = query.split("from");
                 String[] AfterSelect  = beforeFrom[0].split("select");
                 var selectedColumns = SelectedColumns(CurdbName,words[3],AfterSelect[1].trim());
                 if (selectedColumns == null){
                     return false;
                 }
                 if (words.length == 4) {
                     fetch(CurdbName, words[3], selectedColumns, "", null, 0,0);
                    }
                 else {
                     if (!words[4].equals("where")) {
                         System.out.println("Expecting WHERE CLAUSE!");
                         return false;
                     }
                     String[] AfterWhere = query.split("where");
                     if(AfterWhere.length == 1){
                         System.out.println("No Column After where clause!");
                         return false;
                     }
                     String columnName = CheckColumnExist(CurdbName, words[3].trim(), AfterWhere[1]);
                     if(columnName.equals("False")){
                         return false;
                     }

                     var ColumnValues = SendColValues(AfterWhere[1]);
                     if(ColumnValues == null){
                         System.out.println("Column Values is not mentioend!");
                         return false;
                     }
                     if (columnName.equals("False")) {
                         System.out.println("Column Doesnot Exist!");
                         return false;
                     }
                     int Notin = 0;
                     if(query.contains("not in") || query.contains("!=")){
                         Notin = 1;
                     }
                     fetch(CurdbName, words[3], selectedColumns, columnName, ColumnValues, 0,Notin);
                 }
         }
         return false;
    }

    public static HashMap<String,Integer> PrimaryKeyValues(String DbName, String TableName) throws IOException {
        HashMap<String,Integer> res = new HashMap<>();
        String curURL = DbPath + DbName + "/" + TableName;
        String Meta = curURL + "/meta.txt";
        String Data  =curURL + "/data.txt";
        int index = -1;
        File f = new File(Meta);
        BufferedReader bf = new BufferedReader(new FileReader(f));
        String columns = bf.readLine();
        String []cols = columns.split(Pattern.quote(seprator));
        for(int i = 0 ; i < cols.length  ; i ++){
            String[] chkPrimary  = cols[i].trim().split(" ");
            if(chkPrimary.length == 2){
                index = i;
                break;
            }
        }
        if(index == -1){
            return null;
        }
        f = new File(Data);
        bf = new BufferedReader(new FileReader(f));
        String line;
        while((line = bf.readLine()) != null)
        {
            String [] Values = line.split(Pattern.quote(seprator));
            res.put(Values[index].trim() , index);
        }
        return res;
    }

    public static boolean CheckInsert(String query , String DbName) throws IOException {
        query = query.trim();
        query = query.toLowerCase();
        String []words =query.split(" ");
        if(!words[0].trim().equals("insert")){
            System.out.println("PROBLEM IN INSERT KEYWORD!");
            return false;
        }
        if(!words[1].trim().equals("into")){
            System.out.println("Wrong into keyword!");
            return false;
        }
        if(!CheckTableExist(DbName,words[2].trim())){
            System.out.println("Table does not Exist in the corresponding table!");
            return false;
        }
        String curPath = DbPath  + DbName  + "/" + words[2];
        String meta = curPath + "/meta.txt";
        String data = curPath + "/data.txt";

        var Unique = PrimaryKeyValues(DbName,words[2].trim());
        String [] AfterValues = query.split("values");
        ArrayList<String> queryValues = new ArrayList<>();
        String values = AfterValues[1].trim();
        int i  = 0;
        int open = 0;
        while(i < values.length()){
           String temp = "";
           if(values.charAt(i) == '('){
                open++;
                while(i < values.length()){
                    if(values.charAt(i) == ')'){
                        temp += values.charAt(i++);
                        open--;
                        break;
                    }
                        temp += values.charAt(i++);
                    }
                    if(open == 1){
                        System.out.println("Not valid Query!");
                        return false;
                    }
                    queryValues.add(temp);
                }
                else{
                    int comma = 0;
                    while(i < values.length() && values.charAt(i) != '('){
                        if(values.charAt(i) == ','){
                            comma ++;
                        }
                        i++;
                    }
                    if(comma != 1){
                        System.out.println(" Not a valid query: Need a comma between Insertion values!");
                    }
                }
            }
            File  f = new File(meta);
            Scanner sc = new Scanner(f);
            String  TCs  = sc.nextLine();
            String  CDt = sc.nextLine();
            String[] TableColumns = TCs.split(Pattern.quote(seprator));
            String[] ColumnDatatype = CDt.split(Pattern.quote(seprator));
            ArrayList<String> Insert = new ArrayList<>();
            System.out.println(Unique);
            for(String val : queryValues){

                val  = val.replace("(","");
                val = val.replace(")","");
                System.out.println("val : " + val);
                String []colVal = val.split(",");
                if(colVal.length != TableColumns.length){
                    System.out.println("The number of DataInput is more than actual Columns!");
                    return false;
                }

                String InsertTmp = "";
                for(int it = 0; it < colVal.length; it ++){
                    String [] TableTmp = TableColumns[it].split(" ");
                    if(TableTmp.length == 2){
                        if(Unique.containsKey(colVal[it].trim()) && Unique.get(colVal[it]) == it){
                            System.out.println("Duplicate Value found in the Primary key of the table!");
                            return false;
                        }
                    }
                    if(ColumnDatatype[it].trim().equals("int")){
                        if(!isNumeric(colVal[it])){
                            System.out.println("Datatype didn't match!");
                            return false;
                        }
                        InsertTmp += colVal[it].trim();
                        InsertTmp += seprator;
                    }
                    else{
                        InsertTmp += colVal[it].trim();
                        InsertTmp += seprator;
                    }
                }
                System.out.println(InsertTmp);
                Insert.add(InsertTmp);
            }
            FileWriter fw = new FileWriter(data,true);
            for(String s : Insert){
                fw.append(s + "\n");
            }
            fw.close();
        return true;
    }

    public void InvalidQuery(){
        System.out.println();
        System.out.println("The Query is Invalid!");
        System.out.println();
    }
//    public static void main(String args[]) throws IOException {
//
//
//                CreateDatabase("create DATABASE tester");
//                showDatabase();
//                CreateTable("create table kanu (userid varchar(255) FOREIGN KEY REFERENCES anas(userid), username int primary key)","DB1");
//                SelectFromTable("select userid,username from user where userid in (danu)","DB1");
//                CheckUpdate("update user set userid = benny where userid != benny ", "DB1");
//                CheckDelete("delete from user where userid = benny,asd)","DB1");
//                CheckInsert("insert into user values (a,1,c),  (d,2,f)","DB1");
//    }

}








































































