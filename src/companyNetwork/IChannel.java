package companyNetwork;

public interface IChannel {
    String getName();
    void setIntruder(Subscriber intruder);
    Subscriber getParticipant01();
    Subscriber getParticipant02();
    void send(String message, String encryptedMessage, Participant participantFrom, String algorithm, String keyfile, int participantToID);
}
