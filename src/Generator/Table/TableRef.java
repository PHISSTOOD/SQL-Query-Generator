package Generator.Table;

import Generator.Element.NamedRelation;
import Generator.Node.Node;

import java.util.ArrayList;
import java.util.List;

import static Generator.Random.RandomGenerate.randomPick5;

public class TableRef extends Node {

    List<NamedRelation> refs;

    public TableRef(Node parent) {
        super(parent);
        this.refs = new ArrayList<>();
    }

    public static TableRef generate(Node parent){
        if(parent.getLevel()<randomPick5()){
            return new SubQuery(parent);
        }else if(parent.getLevel()<randomPick5()){
            return new JoinTable(parent);
        }else{
            return new TableOrQuery(parent);
        }

    }

    public List<NamedRelation> getRefs() {
        return refs;
    }
}
