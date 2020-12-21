package Generator.Expression;

import Generator.Element.AggregateType;
import Generator.Element.SQLType;
import Generator.Node.Node;
import Generator.Random.RandomPick;

import static Generator.Random.RandomGenerate.randomPick10;
import static Generator.Random.RandomPick.randomPickAggregate;

public class AggregateExpression extends Expression{

    AggregateType aggregationType;
    Expression expression;

    public AggregateExpression(Node parent, SQLType curSqlType) {
        super(parent);
        this.aggregationType = randomPickAggregate();
        if(aggregationType==AggregateType.COUNT){
            expression = new ColumnRef(this,aggregationType.getType());
        }else if(randomPick10()<9){
            expression = new ColumnRef(this,aggregationType.getType());
        }else{
            expression = new OpSelect(this,aggregationType.getType());
        }
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(aggregationType.toString() + "(" + expression.toString() + ")");
        return stringBuilder.toString();
    }


}
