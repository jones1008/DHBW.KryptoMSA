package companyNetwork;

public interface IChannel {
    String getName();
    void setIntruder(Subscriber intruder);
    Subscriber getParticipant01();
    Subscriber getParticipant02();
}
