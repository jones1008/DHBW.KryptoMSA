package companyNetwork;

public abstract class Subscriber {
    protected int id;

    public Subscriber(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}