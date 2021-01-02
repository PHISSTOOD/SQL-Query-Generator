package Generator;

public class Having {

    private String aggregate;
    private String column;
    private String oprator;
    private String comparator;

    public Having(String aggregate,String column, String oprator, String comparator) {
        this.aggregate = aggregate;
        this.column = column;
        this.oprator = oprator;
        this.comparator = comparator;
    }

    public String getAggregate() {
        return aggregate;
    }

    public String getColumn(){
        return this.column;
    }

    public String getOprator() {
        return oprator;
    }

    public String getComparator(){
        return comparator;
    }
}
