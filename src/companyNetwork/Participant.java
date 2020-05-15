package companyNetwork;

import persistence.HSQLDB;

public class Participant extends Subscriber {
    private ParticipantType type;

    public Participant(String name, ParticipantType type) {
        super(HSQLDB.instance.getNextID("participants"), name);
        this.type = type;
    }

    public Participant(int id, String name, ParticipantType type)
    {
        super(id, name);
        this.type = type;
    }

    public boolean equals() {
        // TODO: implementieren
        return false;
    }

    public String getType()
    {
        return type.toString();
    }

//    @Subscribe
//    public void receive(FireEvent fireEvent) {
//        System.out.println("FireBrigade " + id);
//        System.out.println(fireEvent);
//        System.out.println();
//    }
}
