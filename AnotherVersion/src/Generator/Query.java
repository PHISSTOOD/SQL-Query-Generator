package Generator;


import java.util.ArrayList;
import java.util.List;

/*
 Generate a Query SQL
 */
public class Query {

    private String sql;
    private boolean distinct;
    private List<Column> columns;
    private List<Table> tables;
    private List<Join> joins;
    private List<Condition> whereConditions;
    private Group group;
    private List<Order> orderBy;
    private Limit limit;

    private static final String NEW_LINE = System.getProperty("line.separator");


    public Query() {
        this.distinct = false;
        this.columns = new ArrayList();
        this.tables = new ArrayList();
        this.joins = new ArrayList();
        this.whereConditions = new ArrayList();
        this.group = new Group();
        this.orderBy = new ArrayList();
        this.limit = new Limit(-1,-1);
        this.sql = "";

    }

    // Some operations initializing the attributes of a query sql, like set SELECT, FROM, JOIN, GROUP BY...
    public Query setDistinct(boolean distinct){
        this.distinct = distinct;
        return this;
    }

    public Query addColumn(String columnName){
        Column column = new Column(columnName, false, null);
        columns.add(column);
        return this;
    }

    public Query addColumn(String columnName, String columnCode){
        Column column = new Column(columnName, false, columnCode);
        columns.add(column);
        return this;
    }

    public Query addColumn(String columnName, boolean as, String columnCode){
        if(as){
            if(columnCode == null || columnCode.equals("")){
                throw new IllegalArgumentException("empty columnCode");
            }
        }
        Column column = new Column(columnName, as, columnCode);
        columns.add(column);
        return this;
    }

    public Query addTable(String tableName,boolean as, String tableCode, boolean isGeneratedBySQL){
        if(as){
            if(tableCode == null || tableCode.equals("")){
                throw new IllegalArgumentException("empty tableCode");
            }
        }
        Table table = new Table(tableName,as,tableCode,isGeneratedBySQL);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName, boolean isGeneratedBySQL){
        Table table = new Table(tableName,false,null, isGeneratedBySQL);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName, String tableCode){
        Table table = new Table(tableName,false,tableCode,false);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName,boolean as,String tableCode){
        if(as){
            if(tableCode == null || tableCode.equals("")){
                throw new IllegalArgumentException("empty tableCode");
            }
        }
        Table table = new Table(tableName,as,tableCode,false);
        tables.add(table);
        return this;
    }

    public Query addTable(String tableName){
        Table table = new Table(tableName,false,null,false);
        tables.add(table);
        return this;
    }

    public Query addJoin(String joinType,String tableName, String tableCode, String cond1, String cond2, String operator){
        if(!checkJoinType(joinType)){
            throw new IllegalArgumentException("illegal join type");
        }
        Join join = new Join(joinType,tableName,tableCode,cond1,cond2,operator);
        this.joins.add(join);
        return this;
    }

    public Query addJoin(String joinType,String tableName, String tableCode,String using){
        if(!checkJoinType(joinType)){
            throw new IllegalArgumentException("illegal join type");
        }
        Join join = new Join(joinType,tableName,tableCode,using);
        this.joins.add(join);
        return this;
    }

    public Query addJoin(String joinType,String tableName, String tableCode ){
        if(!checkJoinType(joinType)){
            throw new IllegalArgumentException("illegal join type");
        }
        Join join = new Join(joinType, tableName,tableCode, (String)null);
        this.joins.add(join);
        return this;
    }

    public Query addWhereCondition(String cond1, String cond2, String operation, boolean isCond1GeneratedBySQL, boolean isCond2GeneratedBySQL){
        Condition condition = new Condition(cond1,cond2,operation,isCond1GeneratedBySQL,isCond2GeneratedBySQL);
        whereConditions.add(condition);
        return this;
    }

    public Query addWhereCondition(String cond1, String cond2, String operation){
        Condition condition = new Condition(cond1,cond2,operation,false,false);
        whereConditions.add(condition);
        return this;
    }

    public Query addGroupBy(String columnName){
        this.group.addColumn(columnName);
        return this;
    }

    public Query addHaving(String aggregate,String columnName,String oprator,String comparator){
        if(!checkAggregate(aggregate)){
            throw new IllegalArgumentException("illegal aggregate");
        }
        this.group.addHaving(aggregate,columnName,oprator,comparator);
        return this;
    }

    public Query addOrderBy(String orderName, String orderType) {
        if(!checkOrderType(orderType)){
            throw new IllegalArgumentException("illegal order type");
        }
        Order order = new Order(orderName,orderType);
        orderBy.add(order);
        return this;
    }

    public Query addOrderBy(String orderName) {
        Order order = new Order(orderName,null);
        orderBy.add(order);
        return this;
    }

    public Query setLimit(int offset,int size) {
        this.limit = new Limit(offset,size);
        return this;
    }

    public Query setLimit(int size) {
        this.limit = new Limit(0,size);
        return this;
    }

    /*
     Generate the query sql sentence.
     Because in a query sql, the sequence is:
     SELECT -> FROM -> (WHERE) -> (GROUP BY) -> (HAVING) -> (ORDER BY) those clauses in () means they are unnecessary.
     The sequence of the implementation of generating is accord with this sequence.
     */
    public String generate() throws  IllegalArgumentException{
        StringBuilder stringBuilder = new StringBuilder();

        // Add SELECT at the beginning of sql, and add DISTINCT if it has been set.
        stringBuilder.append("SELECT ");
        if(this.distinct){
            stringBuilder.append("DISTINCT ");
        }

        // Add those columns which want to be selected.
        String columnStatement = columnArrayToString(this.columns);
        if(columnStatement != null && !"".equals(columnStatement)){
            stringBuilder.append(columnStatement);
        }else{
            stringBuilder.append("* ");
        }

        // Add FROM, tables, join.
        String tableStatement = tableArrayToString(this.tables);
        if(tableStatement != null && !"".equals(tableStatement)){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append("FROM ");
            stringBuilder.append(tableStatement);
        }else{
            throw new IllegalArgumentException("table name is null");
        }
        String joinStatement = joinArrayToString(joins);
        if(joinStatement != null && !"".equals(joinStatement)){
            stringBuilder.append(joinStatement);
        }

        // Add WHERE and its conditions. If there is no conditions add "WHERE 1 = 1".
        String conditionStatement = whereConditionArrayToString(this.whereConditions);
        if(conditionStatement != null && !"".equals(conditionStatement)){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append("WHERE ");
            stringBuilder.append(conditionStatement);
        }

        // Add GROUP BY and HAVING
        if(this.group.getColumns().size()>0){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append("GROUP BY ");
            String groupByStatement = groupByPrinter(group);
            stringBuilder.append(groupByStatement);
        }

        // Realize ORDER BY.
        String orderByStatement = orderArrayToString(this.orderBy);
        if(this.orderBy != null && !"".equals(orderByStatement)){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append("ORDER BY ");
            stringBuilder.append(orderByStatement);
        }

        // Add limit if user set a limitation > 0.
        if(this.limit.getSize()>0){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append("LIMIT ");
            if(this.limit.getOffset()>0){
                stringBuilder.append(this.limit.getOffset()+", ");
            }
            stringBuilder.append(this.limit.getSize());
        }

        this.sql = stringBuilder.toString();
        return sql;
    }

    /*
     Convert the list of columns which should be added after SELECT to a String.
     */
    private String columnArrayToString(List<Column> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ", ";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(list.get(i).getColumnName());
            if(list.get(i).isAs()){
                stringBuilder.append(" AS " + list.get(i).getColumnCode());
            }else if(list.get(i).getColumnCode()!=null){
                stringBuilder.append(" " + list.get(i).getColumnCode());
            }
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    /*
     Convert the list of tables which should be added after FROM to a String.
     */
    private String tableArrayToString(List<Table> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ", ";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(tablePrinter(list.get(i)));
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    /*
     Convert the lists of joins which should be added after table to a String.
     */
    private String joinArrayToString(List<Join> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = " ";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append(list.get(i).getJoinType() + " ");
            stringBuilder.append(list.get(i).getTable().getTableName());
            if(list.get(i).getTable().getTableCode()!=null){
                stringBuilder.append(" ");
                stringBuilder.append(list.get(i).getTable().getTableCode());
            }
            if(list.get(i).getOn()!=null){
                stringBuilder.append(onPrinter(list.get(i).getOn()));
            }else if(list.get(i).getUsing()!=null){
                stringBuilder.append(" USING " + list.get(i).getUsing());
            }
        }
        return stringBuilder.toString();
    }

    /*
     Convert the list of conditions which should be added after WHERE to a String.
     */
    private String whereConditionArrayToString(List<Condition> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = " AND ";
        for(int i = 0;i<list.size();i++){

            if(list.get(i).getCond1GeneratedBySQL()){
                stringBuilder.append("(" + list.get(i).getCond1() + ")");
            }else{
                stringBuilder.append(list.get(i).getCond1());
            }

            stringBuilder.append(" " + list.get(i).getOperation() + " ");

            if(list.get(i).getCond2GeneratedBySQL()){
                stringBuilder.append("(" + list.get(i).getCond2() + ")");
            }else{
                stringBuilder.append(list.get(i).getCond2());
            }

            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    /*
     Convert a GROUP BY to a String.
     */
    private String groupByPrinter(Group group){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0;i<this.group.getColumns().size();i++){
            stringBuilder.append(group.getColumns().get(i).getColumnName());
            if(i!=group.getColumns().size()-1){
                stringBuilder.append(", ");
            }
        }
        if(group.getHavings().size()!=0){
            stringBuilder.append(NEW_LINE);
            stringBuilder.append("HAVING ");
            for(int i = 0;i<this.group.getHavings().size();i++){
                stringBuilder.append(havingPrinter(group.getHavings().get(i)));
                if(i!=group.getHavings().size()-1){
                    stringBuilder.append(" AND ");
                }
            }
        }
        return stringBuilder.toString().trim();
    }

    /*
     Convert the list of order which should be added after WHERE to a String.
     */
    private String orderArrayToString(List<Order> list) {
        StringBuilder stringBuilder = new StringBuilder();
        String separator = ",";
        for(int i = 0;i<list.size();i++){
            stringBuilder.append(list.get(i).getOrderName());
            if(list.get(i).getOrderType()!=null){
                stringBuilder.append(" " + list.get(i).getOrderType());
            }
            if(i!=list.size()-1){
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString().trim();
    }

    private String tablePrinter(Table table){
        StringBuilder stringBuilder = new StringBuilder();
        if(table.getGeneratedBySQL()){
            stringBuilder.append("(" + table.getTableName().replace(NEW_LINE," ") + ") ");
            if(table.isAs()){
                stringBuilder.append("AS ");
            }
            stringBuilder.append(table.getTableCode());
        }else{
            stringBuilder.append(table.getTableName());
            if(table.getTableCode()!=null){
                stringBuilder.append(" ");
                if(table.isAs()){
                    stringBuilder.append("AS ");
                }
                stringBuilder.append(table.getTableCode());
            }
        }
        return stringBuilder.toString();
    }

    private String onPrinter(On on){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ON ");
        stringBuilder.append(on.getCond1());
        stringBuilder.append(" " + on.getOperation() + " ");
        stringBuilder.append(on.getCond2());
        return stringBuilder.toString();
    }

    private String havingPrinter(Having having){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(having.getAggregate());
        stringBuilder.append("(" + having.getColumn() + ")");
        stringBuilder.append(" " + having.getOprator() + " ");
        stringBuilder.append(having.getComparator());
        return stringBuilder.toString();
    }

    /*
     Check the joinType user type is legal or illegal
     */
    private boolean checkJoinType(String joinType){
        if(joinType.toUpperCase().equals("JOIN") || joinType.toUpperCase().equals("INNER JOIN") || joinType.toUpperCase().equals("LEFT JOIN")
        || joinType.toUpperCase().equals("RIGHT JOIN") || joinType.toUpperCase().equals("LEFT OUTER JOIN") || joinType.toUpperCase().equals("RIGHT OUTER JOIN")
        || joinType.toUpperCase().equals("FULL JOIN") || joinType.toUpperCase().equals("FULL OUTER JOIN") || joinType.toUpperCase().equals("CROSS JOIN")){
            return true;
        }
        return false;
    }

    /*
     Check the orderType user type is legal or illegal
     */
    private boolean checkOrderType(String orderType){
        if(orderType.toUpperCase().equals("ASC") || orderType.toUpperCase().equals("DESC")){
            return true;
        }
        return false;
    }

    /*
     Check the aggregate user type is legal or illegal
     */
    private boolean checkAggregate(String aggregate){
        if(aggregate.toUpperCase().equals("COUNT") || aggregate.toUpperCase().equals("SUM") || aggregate.toUpperCase().equals("AVG")
                || aggregate.toUpperCase().equals("MIN") || aggregate.toUpperCase().equals("MAX")){
            return true;
        }
        return false;
    }

    public String getSql() {
        return sql;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Join> getJoins() {
        return joins;
    }

    public List<Condition> getWhereConditions() {
        return whereConditions;
    }

    public Group getGroup() {
        return group;
    }

    public List<Order> getOrderBy() {
        return orderBy;
    }

    public Limit getLimit() {
        return limit;
    }
}