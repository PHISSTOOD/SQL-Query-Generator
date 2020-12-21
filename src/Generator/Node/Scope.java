package Generator.Node;

import Generator.Element.Column;
import Generator.Element.NamedRelation;
import Generator.Element.SQLType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Scope {
    Scope parent;
    List<NamedRelation> tables;
    List<NamedRelation> columns;
    int seq;

    public Scope(Scope parent) {
        this.parent = parent;
        if(parent!=null){
            tables = parent.tables;
            columns = parent.columns;
            seq = parent.seq;
        }else{
            this.columns = new ArrayList<>();
            this.tables = new ArrayList<>();
            this.seq = 1;
        }
    }

    public List<NamedRelation> getTables() {
        return tables;
    }

    public void setTables(List<NamedRelation> tables) {
        this.tables = tables;
    }

    public List<NamedRelation> getColumns() {
        return columns;
    }

    public void setColumns(List<NamedRelation> columns) {
        this.columns = columns;
    }

    public List<Object[]> columnOfType(SQLType sqlType){
        List<Object[]> result = new ArrayList<>();
        for(NamedRelation namedRelation : columns){
            for(Column column : namedRelation.getColumns()){
                if(column.getSqlType()==sqlType){
                    Object[] cur = new Object[2];
                    cur[0] = namedRelation;
                    cur[1] = column;
                    result.add(cur);
                }
            }
        }
        return result;
    }

    public String aliasGenerate(String prefix){
        String result =prefix + String.valueOf(seq++);
        return result;
    }

    public static void copyScope(Scope scope, Scope curScope){
        curScope.parent = scope.parent;
        curScope.tables = new ArrayList<>(scope.getTables());
        curScope.columns = new ArrayList<>(scope.getColumns());
        curScope.seq = scope.seq;
    }

}
