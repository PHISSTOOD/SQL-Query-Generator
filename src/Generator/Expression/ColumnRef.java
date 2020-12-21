package Generator.Expression;

import Generator.Element.Column;
import Generator.Element.SQLType;
import Generator.Element.Table;
import Generator.Node.Node;
import Generator.Random.RandomPick;

import java.util.List;

import static Generator.Random.RandomGenerate.randomPickSpecific;
import static Generator.Random.RandomPick.randomPickColumn;
import static Generator.Random.RandomPick.randomPickTable;

public class ColumnRef extends Expression{

    String reference;

    public ColumnRef(Node parent, SQLType curSqlType) {
        super(parent);
        reference = "";
        if(curSqlType==null){
            Table table = randomPickTable(this.getScope().getColumns());
            reference += table.getAlias() + ".";
            Column randomColumn = randomPickColumn(table.getColumns());
            reference += randomColumn.getColumnName();
            sqlType = randomColumn.getSqlType();
        }else{
            List<Object[]> list = this.getScope().columnOfType(curSqlType);
            if(list.size()==0){
                throw new IllegalArgumentException("no corresponding column of the specific SQL type");
            }else{
                int index = randomPickSpecific(list.size());
                Object[] entry = list.get(index);
                Table table = (Table)entry[0];
                Column column = (Column)entry[1];
                reference += table.getAlias() + "." + column.getColumnName();
            }
        }

    }

    public String toString(){
        return reference;
    }
}
