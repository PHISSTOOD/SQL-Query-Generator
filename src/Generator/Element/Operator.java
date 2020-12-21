package Generator.Element;

public class Operator {

    String operatorName;
    SQLType left;
    SQLType right;
    SQLType result;

    public Operator(String operatorName, SQLType left, SQLType right, SQLType result) {
        this.operatorName = operatorName;
        this.left = left;
        this.right = right;
        this.result = result;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public SQLType getLeft() {
        return left;
    }

    public SQLType getRight() {
        return right;
    }

    public SQLType getResult() {
        return result;
    }
}
