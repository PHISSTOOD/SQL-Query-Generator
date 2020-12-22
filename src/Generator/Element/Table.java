package Generator.Element;

import java.util.ArrayList;
import java.util.List;

public class Table {
    String name;
    String alias;
    List<Column> columns;

    public Table() {
        this.name = null;
        this.alias = null;
        columns = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public static void copy(Table table,Table target){
        target.name = table.name;
        target.columns = new ArrayList<>(table.columns);
    }

}
