package test.Init;

public class User {
    private int id ;
    private String name ;
    private int num;

    public User(int id, String name, int num) {
        this.id = id;
        this.name = name;
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}
