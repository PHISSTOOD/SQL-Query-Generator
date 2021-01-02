package Distribute;

import Generator.Query;

import java.util.*;

public class Executor {

    public String selectExecutor(Query query){
        if(!isMultipleTable(query)){
            List<Request> requests = new ArrayList<>();
            Request request = new Request("0","0",query.generate());
            requests.add(request);
            Select select = new Select(false,request,requests);
            return select.selectSQL().getSqlResult();
        }else{
            List<Request> requests = new ArrayList<>();
            Request request = new Request("0","0",query.generate());
            int tableNum = query.getTables().size()+query.getJoins().size();
            HashMap<String,Query> table2Query = new HashMap<>(tableNum);
            List<String> tables = new ArrayList<>();
            for(int i = 0;i<query.getTables().size();i++){
                if(query.getTables().get(i).getTableCode().equals("")
                        || query.getTables().get(i).getTableCode() == null){
                    throw new IllegalArgumentException("some tables don't have table alias");
                }
                tables.add(query.getTables().get(i).getTableCode());
            }
            for(int i = 0;i<query.getJoins().size();i++){
                if(query.getJoins().get(i).getTable().getTableCode().equals("")
                        || query.getJoins().get(i).getTable().getTableCode() == null){
                    throw new IllegalArgumentException("some tables don't have table alias");
                }
                tables.add(query.getJoins().get(i).getTable().getTableCode());
            }
            for(int i = 0;i<tables.size();i++){
                Query childQuery = new Query();
                table2Query.put(tables.get(i),childQuery);
            }
            autoFillColumn(table2Query,query);
            autoFillTable(table2Query,query);
            autoFillWhereCondition(table2Query,query);
            for(Map.Entry<String,Query> entry : table2Query.entrySet()){
                Request childRequest = new Request("0","0",entry.getValue().generate(),entry.getKey());
                requests.add(childRequest);
            }
            Select select = new Select(true,request,requests);
            return select.selectSQL().getSqlResult();
        }

    }

    public boolean isMultipleTable(Query query){
        if(query.getTables().size()+query.getJoins().size()>1){
            return true;
        }else {
            return false;
        }
    }

    private void autoFillColumn(HashMap<String,Query> map,Query query){
        for(int i = 0;i<query.getColumns().size();i++){
            String[] curColumn = query.getColumns().get(i).getColumnName().split("\\.");
            map.get(curColumn[0]).addColumn(curColumn[1]);
        }
        for(Query value : map.values()){
            if(value.getColumns().size()==0 || value.getColumns()==null){
                value.addColumn("*");
            }
        }
    }

    private void autoFillTable(HashMap<String,Query> map,Query query){
        for(int i = 0;i<query.getTables().size();i++){
            map.get(query.getTables().get(i).getTableCode()).addTable(query.getTables().get(i).getTableName());
        }
        for(int i = 0;i<query.getJoins().size();i++){
            map.get(query.getJoins().get(i).getTable().getTableCode()).addTable(query.getJoins().get(i).getTable().getTableName());
        }
        for(Query value : map.values()){
            if(value.getTables().size()==0 || value.getTables()==null){
                throw new IllegalArgumentException("something wrong when create child sql's from part");
            }
        }
    }

    private void autoFillWhereCondition(HashMap<String,Query> map,Query query){
        for(int i = 0;i<query.getWhereConditions().size();i++){
            String[] cond1 = query.getWhereConditions().get(i).getCond1().split("\\.");
            String[] cond2 = query.getWhereConditions().get(i).getCond2().split("\\.");
            if((cond1.length>1 && cond2.length>1)||(cond1.length==1 && cond2.length==1)){
                continue;
            }else{
                if(cond1.length>1){
                    map.get(cond1[0]).addWhereCondition(cond1[1],query.getWhereConditions().get(i).getCond2(),query.getWhereConditions().get(i).getOperation());
                }else{
                    map.get(cond2[0]).addWhereCondition(query.getWhereConditions().get(i).getCond1(),cond2[1],query.getWhereConditions().get(i).getOperation());
                }
            }
        }
    }

}
