package Generator.Query;

import Generator.Element.Column;
import Generator.Element.Table;
import Generator.Expression.Expression;
import Generator.Node.Node;

import java.util.ArrayList;
import java.util.List;

import static Generator.Random.RandomGenerate.*;

// // Generate select clause part
public class SelectList extends Node {

    List<Expression> expressions;
    Table derivedColumns;
    int columns;

    public SelectList(Node parent) {
        super(parent);
        columns = 0;
        expressions = new ArrayList<>();
        derivedColumns = new Table();
        do{
            Expression expression = Expression.generate(this, null);
            String newColumnName = this.getScope().aliasGenerate("c_");
            derivedColumns.getColumns().add(new Column(newColumnName, expression.getSqlType()));
            expressions.add(expression);
        } while (randomPick50() < 25);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i<expressions.size();i++){
            stringBuilder.append(expressions.get(i).toString() + " as " + derivedColumns.getColumns().get(i).getColumnName());
            if(i<expressions.size()-1){
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public Table getDerivedColumns() {
        return derivedColumns;
    }
}
