package Generator.Element;

public class Column {

    String columnName;
    SQLType sqlType;

    public Column(String columnName) {
        this.columnName = columnName;
    }

    public Column(String columnName, SQLType sqlType) {
        this.columnName = columnName;
        this.sqlType = sqlType;
    }

    public String getColumnName() {
        return columnName;
    }

    public SQLType getSqlType() {
        return sqlType;
    }
}
