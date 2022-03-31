package analysis;

import java.util.HashMap;
import java.util.Map;

public class Queries {
    private String json = "{\n" +
            "  \"query\": \"update user set userid = benny where userid != benny\",\n" +
            "  \"timestamp\": \"1648724206\",\n" +
            "  \"user\": \"SDEY\",\n" +
            "  \"db\": \"DB1\"\n" +
            "},\n" +
            "{\n" +
            "  \"query\": \"update user set userid = benny where userid != benny\",\n" +
            "  \"timestamp\": \"1648724206\",\n" +
            "  \"user\": \"SDEY\",\n" +
            "  \"db\": \"DB1\"\n" +
            "},\n" +
            "{\n" +
            "  \"query\": \"update user set userid = benny where userid != benny\",\n" +
            "  \"timestamp\": \"1648724206\",\n" +
            "  \"user\": \"Alex\",\n" +
            "  \"db\": \"DB1\"\n" +
            "}";

    private Map<String, QueryDto> countQuery() {
        String[] arrayOfJson= json.split("}");
        Map<String, QueryDto> map = new HashMap<>();

        for(String singleJson: arrayOfJson) {
            String[] arrayOfProperties = singleJson.split("\",");
            String user = arrayOfProperties[2].substring(arrayOfProperties[2].indexOf(":"));
            String db = arrayOfProperties[3].substring(arrayOfProperties[3].indexOf(":"));
            int count;
            if (map.get(user) == null) {
                count = 1;
            } else {
                count = map.get(user).getCount() + 1;
            }
            map.put(user, new QueryDto(db,count));
        }
        return map;
    }

    public void printCountQuery() {
        Map<String, QueryDto> map = countQuery();
        for (Map.Entry<String,QueryDto> entry : map.entrySet()) {
            String formattedName = entry.getKey().replaceAll("[^a-zA-Z0-9]", "");
            String formattedDbName = entry.getValue().getDb().replaceAll("[^a-zA-Z0-9]", "");
            int machine;
            if(formattedDbName.contains("1")) {
                machine = 1;
            } else {
                machine = 2;
            }

            System.out.println("user " + formattedName + " submitted " + entry.getValue().getCount() + " queries for " + formattedDbName + " running on Virtual Machine " + machine) ;
        }
    }

    private Map<String, Integer> countUpdate() {
        String[] arrayOfJson= json.split("}");
        Map<String, Integer> map = new HashMap<>();

        for(String singleJson: arrayOfJson) {
            String[] arrayOfProperties = singleJson.split("\",");
            String query = arrayOfProperties[0].substring(arrayOfProperties[0].indexOf(":"));
            String[] queryParts = query.split(" ");
            String table = queryParts[2];
            if(query.contains("update")) {
                int count;
                if(map.get(table) == null) {
                    count = 1;
                } else {
                    count = map.get(table) + 1;
                }
                map.put(table, count);
            }
        }
        return map;
    }

    public void printCountUpdate() {
        Map<String, Integer> map = countUpdate();
        for (Map.Entry<String,Integer> entry : map.entrySet()) {
            System.out.println("Total " + entry.getValue() + " Update operations are performed on " + entry.getKey());
        }
    }

}
