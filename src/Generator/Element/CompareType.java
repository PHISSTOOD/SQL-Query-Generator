package Generator.Element;

public enum CompareType {

    EQUAL("=",SQLType.INT),
    BIGGER(">",SQLType.INT),
    SMALLER("<",SQLType.INT);

    private String code;
    private SQLType type;

    private CompareType(String code, SQLType type) {
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
