package companyNetwork;

public class SendMessageEvent
{
    private String message;
    private Participant participantFrom;
    private String algorithm;
    private String keyfile;

    public SendMessageEvent(String message, Participant participantFrom, String algorithm, String keyfile)
    {
        this.message = message;
        this.participantFrom = participantFrom;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    public String getMessage() {
        return message;
    }

    public Participant getParticipantFrom()
    {
        return participantFrom;
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public String getKeyfile()
    {
        return keyfile;
    }
}
