package Generator.Query;

import Generator.Expression.BinExpression;
import Generator.Expression.Expression;
import Generator.Node.Node;
import Generator.Node.Scope;

import static Generator.Random.RandomGenerate.*;


public class Query extends Node {

    String distinct;
    SelectList selectList;
    FromClause fromClause;
    Expression whereCondition;
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
        this.fromClause = new FromClause(this);
        this.selectList = new SelectList(this);
        this.whereCondition = BinExpression.generate(this);

        if(randomPick50()>35){
            this.limitClause = "limit " + randomPick100();
        }else{
            this.limitClause = "";
        }
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("line.separator")).append(format(selectList.getLevel())).append("select ").append(this.distinct + " ").append(selectList.toString()).append(System.getProperty("line.separator"));
        stringBuilder.append(format(fromClause.getLevel())).append(fromClause.toString()).append(System.getProperty("line.separator"));
        stringBuilder.append(format(whereCondition.getLevel())).append("where ").append(whereCondition.toString());
        if(limitClause.length()!=0){
            stringBuilder.append(System.getProperty("line.separator")).append(format(fromClause.getLevel())).append(limitClause);
        }
        return stringBuilder.toString();
    }

    public String format(int level){
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

    public FromClause getFromClause() {
        return fromClause;
    }

    public Expression getWhereCondition() {
        return whereCondition;
    }

    public String getLimitClause() {
        return limitClause;
    }

    public Scope getCurScope() {
        return curScope;
    }
}
