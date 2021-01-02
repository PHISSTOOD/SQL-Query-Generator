package Generator;

public class Condition {
    private String cond1;
    private String cond2;
    private String operation;
    private boolean isCond1GeneratedBySQL;
    private boolean isCond2GeneratedBySQL;

    public Condition(String cond1, String cond2, String operation, boolean isCond1GeneratedBySQL, boolean isCond2GeneratedBySQL) {
        this.cond1 = cond1;
        this.cond2 = cond2;
        this.operation = operation;
        this.isCond1GeneratedBySQL = isCond1GeneratedBySQL;
        this.isCond2GeneratedBySQL = isCond2GeneratedBySQL;
    }

    public String getCond1() {
        return cond1;
    }

    public String getCond2() {
        return cond2;
    }

    public String getOperation() {
        return operation;
    }

    public boolean getCond1GeneratedBySQL() {
        return isCond1GeneratedBySQL;
    }

    public boolean getCond2GeneratedBySQL() {
        return isCond2GeneratedBySQL;
    }
}
