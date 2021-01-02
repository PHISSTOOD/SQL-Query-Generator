package Generator;

import java.util.Random;

public class Table {

    private String tableName;
    private boolean as;
    private String tableCode;
    private boolean isGeneratedBySQL;

    public Table(String tableName, boolean as, String tableCode, boolean isGeneratedBySQL){
        this.tableName = tableName;
        this.as = as;
        this.tableCode = tableCode;
        this.isGeneratedBySQL = isGeneratedBySQL;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isAs() {
        return as;
    }

    public String getTableCode() {
        return tableCode;
    }

    public boolean getGeneratedBySQL() {
        return isGeneratedBySQL;
    }


}
