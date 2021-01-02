package Generator;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private List<Column> columns;
    private List<Having> havings;

    public Group() {
        this.columns = new ArrayList<>();
        this.havings = new ArrayList<>();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Having> getHavings() {
        return havings;
    }

    public void addColumn(String colunName){
        this.columns.add(new Column(colunName,false,null));
    }

    public void addHaving(String aggregate,String colunName,String oprator,String comparator){
        this.havings.add(new Having(aggregate,colunName,oprator,comparator));
    }
}
