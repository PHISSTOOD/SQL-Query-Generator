package Generator.Table;


import Generator.Element.Table;
import Generator.Node.Node;

import static Generator.Random.RandomPick.randomPickTable;

public class TableOrQuery extends TableRef {
    Table table;

    public TableOrQuery(Node parent) {
        super(parent);
        table = randomPickTable(this.getScope().getTables());
        table.setAlias(this.getScope().aliasGenerate("table_"));
        this.refs.add(table);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(table.getName() + " as " + table.getAlias());
        return stringBuilder.toString();
    }
}
