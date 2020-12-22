package Generator.Expression;

import Generator.Element.SQLType;
import Generator.Node.Node;
import Generator.Query.SelectList;

import static Generator.Random.RandomGenerate.randomPick8;

public class Expression extends Node {

    SQLType sqlType;
    boolean isAggregate;

    public Expression(Node parent) {
        super(parent);
        isAggregate = false;
    }

    public static Expression generate(Node parent, SQLType sqlType){
        if(randomPick8()<=1 && parent.getLevel()<5 && !(parent instanceof CompareExpression)){
            return new AggregateExpression(parent,sqlType);
        }else if(randomPick8()<1 && parent.getLevel()<5){
            return new ComputeExpression(parent,sqlType);
        }else if(parent.getScope().getColumns().size()!=0 && randomPick8()<3 && !(parent instanceof SelectList)){
            return new ConstExpression(parent,sqlType);
        }else{
            return new ColumnRef(parent,sqlType);
        }
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public boolean isAggregate() {
        return isAggregate;
    }
}
