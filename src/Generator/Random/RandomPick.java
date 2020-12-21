package Generator.Random;

import Generator.Element.*;

import java.util.List;
import java.util.Random;

public class RandomPick {

    public static NamedRelation randomPickNamedRelation(List<NamedRelation> refs){
        if(refs.size()==0 || refs == null){
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(refs.size());
        return refs.get(randomIndex);
    }

    public static Column randomPickColumn(List<Column> columns){
        if(columns.size()==0 || columns == null){
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(columns.size());
        return columns.get(randomIndex);
    }

    public static AggregateType randomPickAggregate(){
        Random random = new Random();
        int index = random.nextInt(5);
        switch (index) {
            case 0:
                return AggregateType.COUNT;
            case 1:
                return AggregateType.AVG;
            case 2:
                return AggregateType.SUM;
            case 3:
                return AggregateType.MAX;
            case 4:
                return AggregateType.MIN;
        }
        return AggregateType.COUNT;

    }

    public static ComputeType randomPickCompute(){
        Random random = new Random();
        int index = random.nextInt(4);
        switch (index) {
            case 0:
                return ComputeType.PLUS;
            case 1:
                return ComputeType.MINUS;
            case 2:
                return ComputeType.TIMES;
            case 3:
                return ComputeType.DIVIDE;
        }
        return ComputeType.PLUS;

    }

    public static CompareType randomPickCompare() {
        Random random = new Random();
        int index = random.nextInt(3);
        switch (index) {
            case 0:
                return CompareType.EQUAL;
            case 1:
                return CompareType.BIGGER;
            case 2:
                return CompareType.SMALLER;
        }
        return CompareType.EQUAL;
    }

}
