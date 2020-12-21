package Generator.Expression;

import Generator.Node.Node;

import static Generator.Random.RandomGenerate.randomPick50;

public class ChildExpression extends BinExpression {

    String connect;

    public ChildExpression(Node parent) {
        super(parent);
        this.connect = (randomPick50()<40)? " and " : " or ";
        this.leftExpression = BinExpression.generate(this);
        this.rightExpression = BinExpression.generate(this);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(" + leftExpression.toString() + ")" + connect + "(" +  rightExpression.toString()+ ")" );
        return stringBuilder.toString();
    }
}
