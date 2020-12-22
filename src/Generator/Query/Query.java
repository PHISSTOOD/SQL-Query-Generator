package Generator.Query;

import Generator.Expression.BinExpression;
import Generator.Expression.Expression;
import Generator.Node.Node;
import Generator.Node.Scope;
import Generator.Random.RandomGenerate;

import static Generator.Random.RandomGenerate.*;

// Generate a Query SQL
public class Query extends Node {

    String distinct;
    SelectList selectList;
    FromClause fromClause;
    Expression whereCondition;
    GroupBy groupBy;
    String limitClause;
    Scope curScope;

    public Query(Node parent, Scope scope) {
        super(parent);
        this.curScope = new Scope(null);
        Scope.copyScope(scope,curScope);
        this.setScope(curScope);
        if(randomPick100()<10){
            this.distinct = "distinct";
        }else{
            this.distinct = "";
        }

        // Initializing each part of a query sql
        this.fromClause = new FromClause(this);
        this.selectList = new SelectList(this);
        this.whereCondition = BinExpression.generate(this);
        boolean needGroupBy = needGroupBy(this.selectList);
        if(needGroupBy && RandomGenerate.randomPick50()<40 ||
                RandomGenerate.randomPick50()<10){
            this.groupBy = new GroupBy(this,this.selectList);
        }

        if(parent!=null && parent.getScope()!=null && this.getScope()!=null){
            parent.getScope().setSeq(this.getScope().getSeq());
        }

        if(randomPick50()>35){
            this.limitClause = "limit " + randomPick100();
        }else{
            this.limitClause = "";
        }
    }

    // print SQL query
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("line.separator")).append(format(selectList.getLevel())).append("select ").append(this.distinct + " ").append(selectList.toString()).append(System.getProperty("line.separator"));
        stringBuilder.append(format(fromClause.getLevel())).append(fromClause.toString()).append(System.getProperty("line.separator"));
        stringBuilder.append(format(whereCondition.getLevel())).append("where ").append(whereCondition.toString());
        if(groupBy!=null){
            stringBuilder.append(System.getProperty("line.separator")).append(format(fromClause.getLevel())).append(groupBy.toString());
        }
        if(limitClause.length()!=0){
            stringBuilder.append(System.getProperty("line.separator")).append(format(fromClause.getLevel())).append(limitClause);
        }
        return stringBuilder.toString();
    }

    // make sure the format of SQL
    public static String format(int level){
        String format = "";
        level -= 4;
        while(level>=0){
            format += "      ";
            level -= 4;
        }
        return format;
    }

    public SelectList getSelectList() {
        return selectList;
    }

    // Estimate is this query need a group by clause
    public boolean needGroupBy(SelectList selectList){
        int countAggregate = 0;
        for(Expression expression : selectList.expressions){
            if(expression.isAggregate()==true){
                countAggregate ++;
            }
        }
        if(countAggregate!=0 && countAggregate != selectList.expressions.size()){
            return true;
        }else{
            return false;
        }
    }
}
