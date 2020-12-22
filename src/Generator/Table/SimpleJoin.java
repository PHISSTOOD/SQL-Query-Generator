package Generator.Table;

import Generator.Element.Column;
import Generator.Element.Table;
import Generator.Node.Node;

import static Generator.Random.RandomPick.randomPickColumn;
import static Generator.Random.RandomPick.randomPickTable;

public class SimpleJoin extends JoinCondition{

    String condition;

    public SimpleJoin(Node parent, TableRef leftTableRef, TableRef rightTableRef) {
        super(parent,leftTableRef,rightTableRef);
        condition = "";
        Table leftRef = randomPickTable(leftTableRef.getRefs());
        Column leftColumn = randomPickColumn(leftRef.getColumns());
        long beginTime = System.currentTimeMillis();
        while(true){
            Table rightRef = randomPickTable(rightTableRef.getRefs());
            Column rightColumn = randomPickColumn(rightRef.getColumns());
            if(leftColumn.getSqlType() == rightColumn.getSqlType()){
                condition += leftRef.getAlias() + "." + leftColumn.getColumnName() + " = " +
                        rightRef.getAlias() + "." + rightColumn.getColumnName();
                break;
            }
            if((System.currentTimeMillis() - beginTime)>500){
                throw new IllegalArgumentException("can't find specific sql type data when generate join condition");
            }
        }
    }

    public String toString(){
        return condition;
    }


}
