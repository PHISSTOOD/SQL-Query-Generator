package Generator.Expression;

import Generator.Element.CompareType;
import Generator.Element.Operator;
import Generator.Element.SQLType;
import Generator.Node.Node;
import Generator.Query.SelectList;

import static Generator.Random.RandomGenerate.randomPick8;
import static Generator.Random.RandomPick.randomPickCompare;

public class CompareExpression extends BinExpression{

    Operator operator;

    public CompareExpression(Node parent) {
        super(parent);
        CompareType compareType = randomPickCompare();
        this.operator = new Operator(compareType.getCode(),compareType.getType(),compareType.getType(), SQLType.BOOLEAN);
        this.leftExpression = Expression.generate(this,operator.getLeft());
        if(this.leftExpression instanceof ConstExpression){
            if(randomPick8() <= 1 && parent.getLevel()<5){
                this.rightExpression = new ComputeExpression(this,operator.getRight());
            }else {
                this.rightExpression = new ColumnRef(this, operator.getRight());
            }
        }else{
            this.rightExpression = Expression.generate(this,operator.getRight());
        }
        this.sqlType = SQLType.BOOLEAN;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.leftExpression.toString() + " " + operator.getOperatorName() + " " + this.rightExpression.toString());
        return stringBuilder.toString();
    }
}
