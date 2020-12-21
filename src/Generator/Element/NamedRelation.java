package Generator.Element;

import java.util.ArrayList;
import java.util.List;

public class NamedRelation {
    String name;
    String alias;
    List<Column> columns;

    public NamedRelation() {
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


}
