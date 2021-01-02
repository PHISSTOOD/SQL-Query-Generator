package Distribute;

import java.util.ArrayList;
import java.util.List;

public class Select {

    private boolean isMultipleTable;
    private Request originalRequest;
    private List<Request> requestList;

    public Select(boolean isMultipleTable,Request originalRequest, List<Request> requestList) {
        this.isMultipleTable = isMultipleTable;
        this.originalRequest = originalRequest;
        this.requestList = requestList;
    }

    public Result selectSQL(){
        if(isMultipleTable){
            List<RPC> rpcs = new ArrayList<>();
            for(int i = 0;i<requestList.size();i++){
                RPC rpc = new RPC(requestList.get(i));
                rpcs.add(rpc);
            }
            List<Result> results = new ArrayList<>();
            for(int i = 0;i<rpcs.size();i++){
                Result result = rpcs.get(i).rpc();
                results.add(result);
            }
            return Union(results);

        }else{
            RPC rpc = new RPC(requestList.get(0));
            Result result = rpc.rpc();
            return result;
        }
    }

    public Result Union(List<Result> results){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<results.size();i++){
            stringBuilder.append(results.get(i).getSqlResult() + " As " + requestList.get(i).getAlias());
            if(i<results.size()-1){
                stringBuilder.append(System.getProperty("line.separator"));
                stringBuilder.append("Result Union ");
            }
        }
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append("With the oringinal sql is (" + originalRequest.getSql().replace(System.getProperty("line.separator")," ") + ")");
        Result result = new Result(stringBuilder.toString());
        return result;
    }
}
