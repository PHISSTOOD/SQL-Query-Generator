package Generator.Node;


public class Node {

    Node parent;
    int level;
    Scope scope;

    public Node(Node parent) {
        this.parent = parent;
        if(parent!=null){
            level = parent.level+1;
            scope = parent.scope;
        }else{
            this.level = 0;
            this.scope = new Scope(null);
        }
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
