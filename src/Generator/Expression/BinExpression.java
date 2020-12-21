package Generator.Expression;

import Generator.Node.Node;

import static Generator.Random.RandomGenerate.randomPick50;

public class BinExpression extends Expression{

    Expression leftExpression;
    Expression rightExpression;

    public BinExpression(Node parent) {
        super(parent);
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }


    public static BinExpression generate(Node parent){
        if(randomPick50()<20){
            return new ChildExpression(parent);
        }else {
            return new CompareExpression(parent);
        }
    }
}
