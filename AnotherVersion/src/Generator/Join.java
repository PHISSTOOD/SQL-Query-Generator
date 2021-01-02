package Generator;

public class Join {
    private String joinType;
    private Table table;
    private On on;
    private String using;

    public Join(String joinType, String tableName, String tableCode, String cond1, String cond2, String operator) {
        this.joinType = joinType;
        this.table = new Table(tableName,false,tableCode,false);
        this.on = new On(cond1,cond2,operator);
        this.using = null;
    }

    public Join(String joinType, String tableName, String tableCode, String using) {
        this.joinType = joinType;
        this.table = new Table(tableName,false,tableCode,false);
        this.on = null;
        this.using = using;
    }

    public String getJoinType() {
        return joinType;
    }

    public Table getTable() {
        return table;
    }

    public On getOn() {
        return on;
    }

    public String getUsing(){
        return this.using;
    }
}
