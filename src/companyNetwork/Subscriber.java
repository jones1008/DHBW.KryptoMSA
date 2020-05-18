package companyNetwork;

import com.google.common.eventbus.Subscribe;

public abstract class Subscriber {
    protected int id;
    protected String name;

    public Subscriber(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}