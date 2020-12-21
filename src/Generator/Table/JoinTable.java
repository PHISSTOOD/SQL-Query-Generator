package Generator.Table;


import Generator.Node.Node;

import static Generator.Random.RandomGenerate.randomPick5;

public class JoinTable extends TableRef {

    String joinType;
    String alias;
    TableRef leftTable;
    TableRef rightTable;
    JoinCondition joinCondition;

    public JoinTable(Node parent) {
        super(parent);
        this.leftTable = TableRef.generate(this);
        this.rightTable = TableRef.generate(this);
        JoinCondition joinCondition = new JoinCondition(this,leftTable,rightTable);
        this.joinCondition = joinCondition.generate(this);
        if(randomPick5()<3){
            this.joinType = "inner";
        }else if(randomPick5()<3){
            this.joinType = "left";
        }else{
            this.joinType = "right";
        }
        this.refs.addAll(leftTable.getRefs());
        this.refs.addAll(rightTable.getRefs());

    }

    public String getJoinType() {
        return joinType;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(leftTable.toString()).append(" " + joinType + " join ").append(rightTable.toString());
        stringBuilder.append(" on (" + joinCondition.toString() + ")");
        return stringBuilder.toString();
    }
}
