package companyNetwork;

import com.google.common.eventbus.Subscribe;
import cryptoManager.CryptoManager;
import persistence.DBPostbox;

public class Participant extends Subscriber {
    private ParticipantType type;

    public Participant(int id, String name, ParticipantType type)
    {
        super(id, name);
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (! (obj instanceof Participant)) {
            return false;
        }

        Participant other = (Participant) obj;

        return this.id == other.id &&
                this.name.equals(other.name) &&
                this.type == other.type;
    }

    public String getType()
    {
        return type.toString();
    }

    public String encrypt (String message, String algorithm, String keyfile) {
        CryptoManager manager = new CryptoManager();
        return manager.encrypt(message, algorithm, keyfile);
    }

    @Subscribe
    public void receive(SendMessageEvent sendMessageEvent) {
        if (sendMessageEvent.getParticipantFromID() == id) {
            return;
        }

        switch (type) {
            case NORMAL:
                receiveAsNormal(sendMessageEvent);
                break;
            case INTRUDER:
                receiveAsIntruder(sendMessageEvent);
                break;
        }
    }

    private void receiveAsNormal(SendMessageEvent sendMessageEvent) {
        CryptoManager manager = new CryptoManager();
        String decrypted = manager.decrypt(sendMessageEvent.getMessage(), sendMessageEvent.getAlgorithm(), sendMessageEvent.getKeyfile());
        DBPostbox.instance.insertDataTablePostbox(this.name, sendMessageEvent.getParticipantFromID(), decrypted);
    }

    private void receiveAsIntruder(SendMessageEvent sendMessageEvent) {
        // TODO: insert postbox: unknown
        // TODO: try to crack
        // TODO: if successful: update postbox
        // TODO: if not: output: crack message failed
    }
}
