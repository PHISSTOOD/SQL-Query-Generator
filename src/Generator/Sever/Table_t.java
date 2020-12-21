package Generator.Sever;

import Generator.Element.Column;
import Generator.Element.NamedRelation;
import Generator.Element.SQLType;

public class Table_t {


    public static NamedRelation addTable_t(){
        NamedRelation namedRelation = new NamedRelation();
        namedRelation.setName("t");
        Column a = new Column("a", SQLType.INT);
        Column b = new Column("b", SQLType.INT);
        Column c = new Column("c", SQLType.STRING);
        namedRelation.getColumns().add(a);
        namedRelation.getColumns().add(b);
        namedRelation.getColumns().add(c);
        return namedRelation;
    }
}
