package Generator.Sever;

import Generator.Element.Column;
import Generator.Element.SQLType;
import Generator.Element.Table;

public class Table_t {


    public static Table addTable_t(){
        Table table = new Table();
        table.setName("t");
        Column a = new Column("a", SQLType.INT);
        Column b = new Column("b", SQLType.INT);
        Column c = new Column("c", SQLType.STRING);
        table.getColumns().add(a);
        table.getColumns().add(b);
        table.getColumns().add(c);
        return table;
    }
}
