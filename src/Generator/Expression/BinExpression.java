package Generator.Expression;

import Generator.Node.Node;

import static Generator.Random.RandomGenerate.randomPick5;
import static Generator.Random.RandomGenerate.randomPick50;

public class BinExpression extends Expression{

    Expression leftExpression;
    Expression rightExpression;

    public BinExpression(Node parent) {
        super(parent);
    }

    public static BinExpression generate(Node parent){
        if(randomPick50()<20 && parent.getLevel()<6+randomPick5()){
            return new ChildExpression(parent);
        }else {
            return new CompareExpression(parent);
        }
    }
}
