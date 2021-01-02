package Distribute;

public class Request {

    private String keyStart;
    private String keyEnd;
    private String sql;
    private String alias;

    public Request(String keyStart, String keyEnd, String sql) {
        this.keyStart = keyStart;
        this.keyEnd = keyEnd;
        this.sql = sql;
    }
    public Request(String keyStart, String keyEnd, String sql,String alias) {
        this.keyStart = keyStart;
        this.keyEnd = keyEnd;
        this.sql = sql;
        this.alias = alias;
    }

    public String getKeyStart() {
        return keyStart;
    }

    public String getKeyEnd() {
        return keyEnd;
    }

    public String getSql() {
        return sql;
    }

    public String getAlias() {
        return alias;
    }
}
