package Generator.Table;

import Generator.Element.NamedRelation;
import Generator.Node.Node;

import static Generator.Random.RandomPick.randomPickNamedRelation;

public class TableOrQuery extends TableRef {
    NamedRelation namedRelation;

    public TableOrQuery(Node parent) {
        super(parent);
        namedRelation = randomPickNamedRelation(this.getScope().getTables());
        namedRelation.setAlias(this.getScope().aliasGenerate("table_"));
        this.refs.add(namedRelation);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(namedRelation.getName() + " as " + namedRelation.getAlias());
        return stringBuilder.toString();
    }
}
