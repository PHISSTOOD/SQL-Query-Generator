package Generator;

public class Column {
    private String columnName;
    private boolean as;
    private String columnCode;

    public Column(String columnName, boolean as, String columnCode){
        this.columnName = columnName;
        this.as = as;
        this.columnCode = columnCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isAs() {
        return as;
    }

    public String getColumnCode() {
        return columnCode;
    }
}
