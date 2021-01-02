package Distribute;

public class RPC {

    private Request request;

    public RPC(Request request) {
        this.request = request;
    }

    public Result rpc(){
        Result result = new Result("Execute (" + request.getSql().replace(System.getProperty("line.separator")," ") + ")");
        return result;
    }
}
