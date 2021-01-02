package test.test;

import Distribute.Executor;
import Generator.Query;
import test.Init.CreateQuery;
import org.junit.Test;

import java.sql.*;
import java.util.Scanner;



public class GenerateTest {

    @Test
    public void testQuery1() {
        try {
            System.out.println();
            Query query1 = new Query().addColumn("*").addTable("t");
            //the target should be: SELECT * FROM t
            System.out.println(query1.generate());
            assert (query1.generate().replace(System.getProperty("line.separator"), " ")
                    .equals("SELECT * FROM t"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testQuery2(){
        try{
            Query query2 = new Query().addColumn("a+b").addTable("t")
                    .addWhereCondition("a","10",">").addWhereCondition("b","20","<");
            //the target should be: SELECT a+b FROM t WHERE a > 10 AND b < 20
            System.out.println(query2.generate());
            assert(query2.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT a+b FROM t WHERE a > 10 AND b < 20"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery3(){
        System.out.println();
        try{
            Query query3 = new Query().addColumn("a").addColumn("c")
                    .addTable("t").addOrderBy("a").setLimit(10);
            //the target should be: SELECT a, c FROM t ORDER BY a LIMIT 10
            System.out.println(query3.generate());
            assert(query3.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT a, c FROM t ORDER BY a LIMIT 10"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery4(){
        System.out.println();
        try{
            Query query4 = new Query().addColumn("*")
                    .addTable("t","t1").addTable("t","t2")
                    .addWhereCondition("t1.a","t2.a","=");
            //the target should be: SELECT * FROM t t1, t t2 WHERE t1.a = t2.a
            System.out.println(query4.generate());
            assert(query4.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT * FROM t t1, t t2 WHERE t1.a = t2.a"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery5(){
        System.out.println();
        try{
            Query query5 = new Query().addColumn("*")
                    .addTable(new Query().addColumn("a").addTable("t").addWhereCondition("a","10","<").generate(),false,"tx",true)
                    .addWhereCondition("tx.a","10",">");
            //the target should be: SELECT * FROM (SELECT a FROM t WHERE a < 10) tx WHERE tx.a > 10
            System.out.println(query5.generate());
            assert(query5.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT * FROM (SELECT a FROM t WHERE a < 10) tx WHERE tx.a > 10"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery6(){
        System.out.println();
        System.out.println("其他自己创建的测试：");

        System.out.println();
        try{
            Query query6 = new Query().addColumn("id").addColumn("num")
                    .addTable("t").addWhereCondition("name","\'jack\'","=");
            //the target should be: SELECT id, num FROM t WHERE name = 'jack'
            System.out.println(query6.generate());
            assert(query6.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT id, num FROM t WHERE name = 'jack'"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery7(){
        System.out.println();
        try{
            Query query7 = new Query().addColumn("a.id").addColumn("b.name").addColumn("c.num")
                    .addTable("t","a").addJoin("inner join","t","b")
                    .addJoin("left join","t","c","a.id","c.num","=")
                    .setLimit(1,10);
            //the target should be: SELECT a.id, b.name, c.num FROM t a inner join t b left join t c ON a.id = c.num LIMIT 1, 10
            System.out.println(query7.generate());
            assert(query7.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT a.id, b.name, c.num FROM t a inner join t b left join t c ON a.id = c.num LIMIT 1, 10"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery8(){
        System.out.println();
        try{
            Query query8 = new Query().addColumn("id").addColumn("avg(num)")
                    .addTable("t").addGroupBy("id").addHaving("count","num",">","20")
                    .addOrderBy("id","DESC").setLimit(5);
            //the target should be: SELECT id, avg(num) FROM t GROUP BY id HAVING count(num) > 20 ORDER BY id DESC LIMIT 5
            System.out.println(query8.generate());
            assert(query8.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT id, avg(num) FROM t GROUP BY id HAVING count(num) > 20 ORDER BY id DESC LIMIT 5"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void testQuery9(){
        System.out.println();
        try{
            Query query9 = new Query().addColumn("SUM(L.EXTENDEDPRICE)/7.0",true,"AVGYEARLY")
                    .addTable("LINEITEM","L").addTable("PART","P")
                    .addWhereCondition("P.PARTKEY","L.PARTKEY","=").addWhereCondition("P.BRAND","\'Brand#23\'","=")
                    .addWhereCondition("P.CONTAINER","\'MED BOX\'","=")
                    .addWhereCondition("L.QUANTITY",new Query().addColumn("0.2*AVG(L1.QUANTITY)")
                            .addTable("LINEITEM","L1").addWhereCondition("L1.PARTKEY","P.PARTKEY","=").generate()
                            ,"<",false,true);
            //the target should be:
            /* SELECT SUM(L.EXTENDEDPRICE)/7.0 AS AVGYEARLY FROM LINEITEM L, PART P
            WHERE P.PARTKEY = L.PARTKEY AND P.BRAND = 'Brand#23' AND P.CONTAINER = 'MED BOX'
            AND L.QUANTITY < (SELECT 0.2*AVG(L1.QUANTITY) FROM LINEITEM L1 WHERE L1.PARTKEY = P.PARTKEY)*/
            System.out.println(query9.generate());
            assert(query9.generate().replace(System.getProperty("line.separator")," ")
                    .equals("SELECT SUM(L.EXTENDEDPRICE)/7.0 AS AVGYEARLY FROM LINEITEM L, PART P WHERE P.PARTKEY = L.PARTKEY AND P.BRAND = 'Brand#23' AND P.CONTAINER = 'MED BOX' AND L.QUANTITY < (SELECT 0.2*AVG(L1.QUANTITY) FROM LINEITEM L1 WHERE L1.PARTKEY = P.PARTKEY)"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


    @Test
    public void testQuery10(){
        System.out.println();
        System.out.println("包含错误的输入：");
        System.out.println();
        try{
            Query query9 = new Query().addColumn("id").addColumn("avg(num)")
                    .addTable("t").addGroupBy("id").addHaving("cont","num",">","20")
                    .addOrderBy("id","DESC").setLimit(5);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testQuery11(){
        System.out.println();
        System.out.println("包含错误的输入：");
        try{
            Query query10 = new Query().addColumn("id").addColumn("avg(num)")
                    .addTable("t").addJoin("left jon","t","c","name")
                    .addOrderBy("id","DESC").setLimit(5);
            System.out.println(query10.generate());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
