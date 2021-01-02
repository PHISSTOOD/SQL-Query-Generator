package test.test;

import Distribute.Executor;
import Generator.Query;
import test.Init.CreateQuery;

import java.sql.SQLException;
import java.util.Scanner;

public class TerminalTest {

    public static void main(String args[]) throws SQLException {

        Scanner input=new Scanner(System.in);
        int distribute = 0;
        while(true){
            System.out.println("type if you want to test it as a distribute data base or MySQL:");
            System.out.println("1 means distribute data base, 2 means only generate SQL for MySQL, 3 to exit");
            distribute = input.nextInt();

            if(distribute==1){
                try {
                    Query queryUser = new Query();
                    queryUser = CreateQuery.createQuery(queryUser);
                    Executor executor = new Executor();
                    System.out.println(executor.selectExecutor(queryUser));
                } catch (Exception e){
                    System.out.println();
                    System.out.println("------------------------------");
                    System.out.println(e.getMessage());
                    System.out.println("------------------------------");
                    System.out.println();
                }
            }else if(distribute==2){
                try {
                    Query queryUser = new Query();
                    queryUser = CreateQuery.createQuery(queryUser);
                    System.out.println(queryUser.generate());
                } catch (Exception e){
                    System.out.println();
                    System.out.println("------------------------------");
                    System.out.println(e.getMessage());
                    System.out.println("------------------------------");
                    System.out.println();
                }
            }else{
                System.out.println("Incorrect input");
            }
        }

    }
}
