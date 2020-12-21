package Generator.Element;

public enum AggregateType {
    COUNT("COUNT",null),
    AVG("AVG",SQLType.INT),
    MIN("MIN",SQLType.INT),
    MAX("MAX",SQLType.INT),
    SUM("SUM",SQLType.INT);


    private String code;
    private SQLType type;

    private AggregateType(String code, SQLType type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SQLType getType() {
        return type;
    }

    public void setType(SQLType type) {
        this.type = type;
    }
}
