package Generator.Table;

import Generator.Expression.BinExpression;
import Generator.Expression.Expression;
import Generator.Node.Node;
import Generator.Node.Scope;

public class JoinExpression extends JoinCondition {

    Scope joinScope;
    BinExpression binExpression;

    public JoinExpression(Node parent,TableRef leftTableRef, TableRef rightTableRef) {
        super(parent,leftTableRef,rightTableRef);
        this.joinScope = parent.getScope();
        this.joinScope.getColumns().addAll(leftTableRef.getRefs());
        this.joinScope.getColumns().addAll(rightTableRef.getRefs());
    }
}
