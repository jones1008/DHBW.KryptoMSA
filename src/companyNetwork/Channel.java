package companyNetwork;

import com.google.common.eventbus.EventBus;

public class Channel implements IChannel {
    private int id;
    private EventBus eventBus;
    private Subscriber participant01;
    private Subscriber participant02;
    private String name;
    private Subscriber intruder;

    public void setIntruder(Subscriber intruder) {
        this.intruder = intruder;
    }
}
