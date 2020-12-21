package Generator.Table;


import Generator.Element.Table;
import Generator.Node.Node;
import Generator.Query.Query;

public class SubQuery extends TableRef {

    Query query;
    String alias;

    public SubQuery(Node parent) {
        super(parent);
        query = new Query(this,this.getScope());
        Table table = query.getSelectList().getDerivedColumns();
        table.setAlias(this.getScope().aliasGenerate("sub_"));
        alias = table.getAlias();
        this.refs.add(table);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(" + query.toString() + ") as " + alias);
        return stringBuilder.toString();
    }
}
