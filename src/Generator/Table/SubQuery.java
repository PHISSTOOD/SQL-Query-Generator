package Generator.Table;


import Generator.Element.NamedRelation;
import Generator.Node.Node;
import Generator.Query.Query;

public class SubQuery extends TableRef {

    Query query;
    String alias;

    public SubQuery(Node parent) {
        super(parent);
        query = new Query(this,this.getScope());
        NamedRelation namedRelation = query.getSelectList().getDerivedColumns();
        namedRelation.setAlias(this.getScope().aliasGenerate("sub_"));
        alias = namedRelation.getAlias();
        this.refs.add(namedRelation);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(" + query.toString() + ") as " + alias);
        return stringBuilder.toString();
    }
}
