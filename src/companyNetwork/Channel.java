package companyNetwork;

import com.google.common.eventbus.EventBus;
import persistence.*;

public class Channel implements IChannel {
    private EventBus eventBus;
    private Subscriber participant01;
    private Subscriber participant02;
    private String name;

    private Subscriber intruder;

    public Channel(String name, Subscriber participant01, Subscriber participant02) {
        this.name = name;
        this.participant01 = participant01;
        this.participant02 = participant02;
        this.eventBus = new EventBus(name);
        eventBus.register(participant01);
        eventBus.register(participant02);
    }

    public void send(String message, String encryptedMessage, Participant participantFrom, String algorithm, String keyfile, int participantToID) {
        int algorithmID = DBAlgorithm.instance.getAlgorithmID(algorithm);
        DBMessage.instance.insertDataTableMessages(participantFrom.getId(), participantToID, message, algorithmID, encryptedMessage, keyfile);
        eventBus.post(new SendMessageEvent(encryptedMessage, participantFrom, algorithm, keyfile));
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
