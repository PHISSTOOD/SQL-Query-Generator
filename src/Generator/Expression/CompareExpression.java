package Generator.Expression;

import Generator.Element.CompareType;
import Generator.Element.Operator;
import Generator.Element.SQLType;
import Generator.Node.Node;

import static Generator.Random.RandomPick.randomPickCompare;

public class CompareExpression extends BinExpression{

    Operator operator;

    public CompareExpression(Node parent) {
        super(parent);
        CompareType compareType = randomPickCompare();
        this.operator = new Operator(compareType.getCode(),compareType.getType(),compareType.getType(), SQLType.BOOLEAN);
        this.leftExpression = Expression.generate(this,operator.getLeft());
        this.rightExpression = Expression.generate(this,operator.getRight());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.leftExpression.toString() + " " + operator.getOperatorName() + " " + this.rightExpression.toString());
        return stringBuilder.toString();
    }
}
