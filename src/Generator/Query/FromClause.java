package Generator.Query;



import Generator.Element.Table;
import Generator.Node.Node;
import Generator.Table.TableRef;

import java.util.ArrayList;
import java.util.List;

public class FromClause extends Node {
    List<TableRef> tableRefs;

    public FromClause(Node parent) {
        super(parent);
        this.tableRefs = new ArrayList<>();
        TableRef tableRef = TableRef.generate(this);
        tableRefs.add(tableRef);
        for(Table o : tableRef.getRefs()){
            this.getScope().getColumns().add(o);
        }
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        if(this.tableRefs.size()!=0){
            stringBuilder.append("from ");
            for(int i = 0;i<tableRefs.size();i++){
                stringBuilder.append(tableRefs.get(i).toString());
                if(i<tableRefs.size()-1){
                    stringBuilder.append(", ");
                }
            }
        }
        return stringBuilder.toString();
    }
}
