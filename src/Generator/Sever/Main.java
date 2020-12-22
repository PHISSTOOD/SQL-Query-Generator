package Generator.Sever;


import Generator.Element.Table;
import Generator.Node.Scope;
import Generator.Query.Query;
import com.sun.tools.javac.util.Name;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        Scanner input=new Scanner(System.in);
        System.out.println("type the number of sql sentence you want to generate");
        int num = input.nextInt();
        Map<String,Integer> errorReport = new HashMap<>();
        for(int i = 0;i<num;i++){
            Scope scope = new Scope(null);
            Table table = Table_t.addTable_t();
            scope.getTables().add(table);
            try{
                Query newQuery = new Query(null,scope);
                System.out.println(newQuery.toString());
                System.out.println();
            } catch (IllegalArgumentException e){
                errorReport.put(e.getMessage(),errorReport.getOrDefault(e.getMessage(),0)+1);
            }
        }
        System.out.println("===the report of error generation:===");
        int success = num;
        for( Map.Entry<String, Integer> entry : errorReport.entrySet()){
            success -= entry.getValue();
        }
        System.out.println("Successfully generated"+"\t\t" + success);
        for(Map.Entry<String, Integer> entry : errorReport.entrySet()){
            System.out.println(entry.getKey()+"\t\t" + entry.getValue());
        }

    }

}
