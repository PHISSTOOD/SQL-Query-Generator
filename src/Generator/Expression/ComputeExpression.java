package Generator.Expression;

import Generator.Element.ComputeType;
import Generator.Element.Operator;
import Generator.Element.SQLType;
import Generator.Node.Node;
import Generator.Random.RandomPick;

public class ComputeExpression extends BinExpression{
    Operator operator;

    public ComputeExpression(Node parent, SQLType curSqlType) {
        super(parent);
        RandomPick randomPick = new RandomPick();
        ComputeType computeType = randomPick.randomPickCompute();
        operator = new Operator(computeType.getCode(),computeType.getType(),computeType.getType(),computeType.getType());
        leftExpression = new ColumnRef(this,operator.getLeft());
        rightExpression = new ColumnRef(this,operator.getRight());
        this.sqlType = SQLType.INT;

    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(leftExpression.toString()).append(" " + operator.getOperatorName() + " ").append(rightExpression.toString());
        return stringBuilder.toString();
    }
}
