package Generator.Query;

import Generator.Element.AggregateType;
import Generator.Element.CompareType;
import Generator.Element.SQLType;
import Generator.Expression.ColumnRef;
import Generator.Expression.ConstExpression;
import Generator.Node.Node;
import Generator.Random.RandomGenerate;
import Generator.Random.RandomPick;

import static Generator.Random.RandomPick.randomPickAggregate;

// Generate group by clause part
public class GroupBy extends Node {
    String columnRef;
    String having;
    AggregateType aggregateType;
    CompareType compareType;
    ConstExpression constExpression;

    public GroupBy(Node parent,SelectList selectList) {
        super(parent);
        int countAggregate = 0;
        columnRef = "";
        having = "";

        // generate the columnRef of group by
        for( int i = 0; i < selectList.expressions.size();i++){
            if(selectList.expressions.get(i).isAggregate()){
                countAggregate++;
            }
        }
        int random = RandomGenerate.randomPickSpecific(selectList.expressions.size()-countAggregate);
        int help = 0;
        String expression = "";
        for( int i = 0; i < selectList.expressions.size();i++){
            if(selectList.expressions.get(i).isAggregate()){
                help++;
            }
            if(help+random==i){
                expression += selectList.expressions.get(i).toString();
                break;
            }
        }
        String[] split = expression.split(" ");
        columnRef += split[0];

        // generate the condition of having
        if(RandomGenerate.randomPick50()<30){
            String helper = columnRef;
            expression = "";
            long beginTime = System.currentTimeMillis();

            //make sure the column in having is not same as the column in group
            while (helper.equals(columnRef)) {
                random = RandomGenerate.randomPickSpecific(selectList.expressions.size());
                expression += selectList.expressions.get(random).toString();
                helper = "";
                if(selectList.expressions.get(random).isAggregate()){
                    String[] split1 = expression.split(" ");
                    having += split1[0];
                    helper += split1[0];
                }else{
                    if(selectList.expressions.get(random).getSqlType()== SQLType.INT){
                        aggregateType = randomPickAggregate();
                    }else{
                        aggregateType = AggregateType.COUNT;
                    }
                    String[] split1 = expression.split(" ");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(aggregateType.getCode()+"("+split1[0]+")");
                    having += stringBuilder.toString();
                    helper += split1[0];
                }
                if((System.currentTimeMillis() - beginTime)>100){
                    throw new IllegalArgumentException("can't find specific sql type data when generate having condition");
                }
            }
            compareType = RandomPick.randomPickCompare();
            constExpression = new ConstExpression(this,SQLType.INT);

        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("group by " + columnRef);
        if(having!=null && having.length()!=0){
            stringBuilder.append(System.getProperty("line.separator")).append(Query.format(this.getLevel()))
                    .append("having " + having + compareType.getCode() + constExpression.toString());
        }
        return stringBuilder.toString();
    }


}
