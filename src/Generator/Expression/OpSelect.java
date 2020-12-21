package Generator.Expression;

import Generator.Element.ComputeType;
import Generator.Element.Operator;
import Generator.Element.SQLType;
import Generator.Node.Node;
import Generator.Random.RandomPick;

public class OpSelect extends Expression {

    Operator operator;
    ColumnRef leftColumnRef;
    ColumnRef rightColumnRef;

    public OpSelect(Node parent, SQLType curSqlType) {
        super(parent);
        RandomPick randomPick = new RandomPick();
        ComputeType computeType = randomPick.randomPickCompute();
        operator = new Operator(computeType.getCode(),computeType.getType(),computeType.getType(),computeType.getType());
        leftColumnRef = new ColumnRef(this,operator.getLeft());
        rightColumnRef = new ColumnRef(this,operator.getRight());

    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(leftColumnRef.toString()).append(" " + operator.getOperatorName() + " ").append(rightColumnRef.toString());
        return stringBuilder.toString();
    }
}
