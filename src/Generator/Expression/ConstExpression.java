package Generator.Expression;

import Generator.Element.SQLType;
import Generator.Node.Node;

import static Generator.Random.RandomGenerate.randomPick100;
import static Generator.Random.RandomGenerate.randomString;

public class ConstExpression extends Expression{
    String expression;

    public ConstExpression(Node parent, SQLType sqlType) {
        super(parent);
        if(sqlType==null){
            sqlType = SQLType.INT;
        }
        if(sqlType == SQLType.INT){
            expression = String.valueOf(randomPick100());
        }else if(sqlType == SQLType.STRING){
            expression = randomString();
        }else{
            expression = "null";
        }
    }

    @Override
    public String toString() {
        return expression;
    }
}
