package companyNetwork;

public class SendMessageEvent
{
    private String message;
    private int participantFromID;
    private String algorithm;
    private String keyfile;

    public SendMessageEvent(String message, int participantFromID, String algorithm, String keyfile)
    {
        this.message = message;
        this.participantFromID = participantFromID;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    public String getMessage() {
        return message;
    }

    public int getParticipantFromID()
    {
        return participantFromID;
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
