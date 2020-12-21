package Generator.Table;

import Generator.Node.Node;

public class JoinCondition extends Node {

    TableRef leftTable;
    TableRef rightTable;

    public JoinCondition(Node parent, TableRef leftTable, TableRef rightTable) {
        super(parent);
        this.leftTable = leftTable;
        this.rightTable = rightTable;

    }

    public JoinCondition generate(Node parent){
        return new SimpleJoin(this,leftTable,rightTable);
    }

}
