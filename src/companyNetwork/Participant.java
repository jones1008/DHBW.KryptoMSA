package companyNetwork;

public class Participant extends Subscriber {
    private ParticipantType type;

    public Participant(int id) {
        super(id);
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

    public ParticipantType getType()
    {
        return type;
    }

//    @Subscribe
//    public void receive(FireEvent fireEvent) {
//        System.out.println("FireBrigade " + id);
//        System.out.println(fireEvent);
//        System.out.println();
//    }
}
