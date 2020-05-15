package companyNetwork;

import com.google.common.eventbus.EventBus;

public class Channel implements IChannel {
    private EventBus eventBus;
    private Subscriber participant01;
    private Subscriber participant02;
    private String name;
    private Subscriber intruder;

    public Channel(Subscriber participant01, Subscriber participant02, String name)
    {
        this.participant01 = participant01;
        this.participant02 = participant02;
        this.name = name;
        this.eventBus = new EventBus();
        //this.eventBus.register(this.participant01);
        //this.eventBus.register(this.participant02);
    }

    public void setIntruder(Subscriber intruder) {
        this.intruder = intruder;
        this.eventBus.register(this.intruder);
    }

    public String getName() {
        return name;
    }

    public Subscriber getParticipant01()
    {
        return participant01;
    }

    public Subscriber getParticipant02()
    {
        return participant02;
    }
}
