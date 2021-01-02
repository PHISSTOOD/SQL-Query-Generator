package test.Init;

import Generator.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CreateQuery {

    public static Query createQuery(Query query){
        List<String> columns = createColumns();
        if(columns.size()==0){
            query.addColumn("*");
        }else {
            for (int i = 0; i < columns.size(); i++) {
                String[] cur = columns.get(i).split("\\|");
                if (cur.length == 2) {
                    query.addColumn(cur[0], cur[1]);
                } else {
                    query.addColumn(cur[0]);
                }
            }
        }

        List<String> tables = createTable();
        for (int i = 0; i < tables.size(); i++) {
            String[] cur = tables.get(i).split("\\|");
            if (cur.length == 2) {
                query.addTable(cur[0], cur[1]);
            } else {
                query.addTable(cur[0]);
            }
        }

        List<String> joins = createJoin();
        if(joins.size()!=0){
            for (int i = 0; i < joins.size(); i++) {
                String[] cur = joins.get(i).split("\\|");
                if (cur.length == 6) {
                    query.addJoin(cur[0], cur[1],cur[2],cur[3],cur[5],cur[4]);
                } else if(cur.length==4){
                    query.addJoin(cur[0], cur[1],cur[2],cur[3]);
                } else if(cur.length==3){
                    query.addJoin(cur[0], cur[1],cur[2]);
                } else {
                    throw new IllegalArgumentException("wrong join input");
                }
            }
        }

        List<String> whereConditions = createWhereCondition();
        if(whereConditions.size()!=0){
            for (int i = 0; i < whereConditions.size(); i++) {
                String[] cur = whereConditions.get(i).split("\\|");
                if (cur.length == 3) {
                    query.addWhereCondition(cur[0], cur[2],cur[1]);
                } else {
                    throw new IllegalArgumentException("wrong condition input");
                }
            }
        }

        List<String> groups = createGroup();
        if(groups.size()!=0){
            for (int i = 0; i < groups.size(); i++) {
                String[] cur = groups.get(i).split("\\|");
                if (cur.length == 4) {
                    query.addGroupBy(cur[0]);
                    query.addHaving(cur[1],cur[0],cur[2],cur[3]);
                } else if(cur.length == 1){
                    query.addGroupBy(cur[0]);
                } else{
                    throw new IllegalArgumentException("wrong group by input");
                }
            }
        }

        List<String> orders = createOrder();
        if(orders.size()!=0){
            for (int i = 0; i < orders.size(); i++) {
                String[] cur = orders.get(i).split("\\|");
                if (cur.length == 2) {
                    query.addOrderBy(cur[0],cur[1]);
                } else if(cur.length == 1){
                    query.addOrderBy(cur[0]);
                } else{
                    throw new IllegalArgumentException("wrong order by input");
                }
            }
        }

        String limit = setLimit();
        if(!limit.equals("")){
            String[] cur = limit.split("\\|");
            if (cur.length == 2) {
                query.setLimit(Integer.parseInt(cur[0]),Integer.parseInt(cur[1]));
            } else if(cur.length == 1){
                query.setLimit(Integer.parseInt(cur[0]));
            } else{
                throw new IllegalArgumentException("wrong limit input");
            }
        }

        return query;
    }

    public static List<String> createColumns(){
        System.out.println();
        System.out.println("please type the columns' name you want to select");
        System.out.println("you can input multiple columns and each column can has an alias");
        System.out.println("you can separate each column by \',\', and separate column name and alias by \'|\' ");
        System.out.println("like this:  id,userName|usr ");
        System.out.println("if you want to skip which means select all columns, type SKIP");
        Scanner input=new Scanner(System.in);
        String column = input.next();
        if(column.trim().toUpperCase().equals("SKIP")){
            return new ArrayList();
        }
        List<String> columns = Arrays.asList(column.split("\\,"));
        return columns;
    }

    public static List<String> createTable(){
        System.out.println();
        System.out.println("please type the tables' name you want to select from");
        System.out.println("you can input multiple tables and each table can has an alias");
        System.out.println("you can separate each table by \',\', and separate table name and alias by \'|\' ");
        System.out.println("like this:  t|a,t|b ");
        Scanner input=new Scanner(System.in);
        String table = input.next();
        List<String> tables = Arrays.asList(table.split("\\,"));
        return tables;
    }

    public static List<String> createJoin(){
        System.out.println();
        System.out.println("please type the join table you want");
        System.out.println("you can input multiple joins, each join could have 3,4,6 part, you can choose which one you want:");
        System.out.println("6 part: jointype, table name, table alias, on conditions's left part, right part, operator");
        System.out.println("4 part: jointype, table name, table alias, using which column to join");
        System.out.println("3 part: jointype, table name, table alias ");
        System.out.println("jointype includes: join, left join, right join");
        System.out.println("you can separate each joins by \',\', and separate each part by \'|\' ");
        System.out.println("like this:  left join|t|c|a.id|=|c.id,join|t|d ");
        System.out.println("if you want to skip which means no join in this SQL query, type SKIP");
        Scanner input=new Scanner(System.in);
        input.useDelimiter("\n");
        String join = input.next();
        if(join.trim().toUpperCase().equals("SKIP")){
            return new ArrayList();
        }
        List<String> joins = Arrays.asList(join.split("\\,"));
        return joins;
    }

    public static List<String> createWhereCondition(){
        System.out.println();
        System.out.println("please type the where conditions you want");
        System.out.println("you can input multiple conditions, each condition have three parts: left part,operator, right part");
        System.out.println("you can separate each condition by \',\', and separate each part by \'|\' ");
        System.out.println("like this:  id|=|1,num|>|8 ");
        System.out.println("if you want to skip which means no WHERE in this SQL, type SKIP");
        Scanner input=new Scanner(System.in);
        String condition = input.next();
        if(condition.trim().toUpperCase().equals("SKIP")){
            return new ArrayList();
        }
        List<String> conditions = Arrays.asList(condition.split("\\,"));
        return conditions;
    }

    public static List<String> createGroup(){
        System.out.println();
        System.out.println("please assign which columns should be used to do the GROUP BY");
        System.out.println("you can input multiple columns, each group input by could has 1 part or 4 part:");
        System.out.println("4 part: column name, Having's aggregate name, HAVING's operator, HAVING's condition");
        System.out.println("1 part: column name");
        System.out.println("aggregate includes: count, sum, avg, max, min");
        System.out.println("you can separate each GROUP by \',\', and separate each part by \'|\' ");
        System.out.println("like this:  id,num|sum|>|30 ");
        System.out.println("if you want to skip which means no GROUP BY in this SQL, type SKIP");
        Scanner input=new Scanner(System.in);
        String group = input.next();
        if(group.trim().toUpperCase().equals("SKIP")){
            return new ArrayList();
        }
        List<String> groups = Arrays.asList(group.split("\\,"));
        return groups;
    }

    public static List<String> createOrder(){
        System.out.println();
        System.out.println("please assign which columns should be used to do the ORDER BY");
        System.out.println("you can input multiple columns, each order input by could has 1 or 2 part:");
        System.out.println("2 part: column name, order type");
        System.out.println("1 part: column name");
        System.out.println("order tyoe includes: ASC, DESC");
        System.out.println("you can separate each order by \',\', and separate each part by \'|\' ");
        System.out.println("like this:  id,num|DESC ");
        System.out.println("if you want to skip which means no ORDER BY in this SQL, type SKIP");
        Scanner input=new Scanner(System.in);
        String order = input.next();
        if(order.trim().toUpperCase().equals("SKIP")){
            return new ArrayList();
        }
        List<String> orders = Arrays.asList(order.split("\\,"));
        return orders;
    }

    public static String setLimit(){
        System.out.println();
        System.out.println("please type the limitation you want");
        System.out.println("the limitation could has 1 or 2 part:");
        System.out.println("2 part: offset, size");
        System.out.println("1 part: size");
        System.out.println("you can separate each part by \'|\' ");
        System.out.println("like this:  1|10 ");
        System.out.println("if you want to skip which means no LIMIT in this SQL, type SKIP");
        Scanner input=new Scanner(System.in);
        String limit = input.next();
        if(limit.trim().toUpperCase().equals("SKIP")){
            return "";
        }
        return limit;
    }
}
