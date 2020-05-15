package companyNetwork;

public class Participant extends Subscriber {
    private String name;
    private ParticipantType type;

    public Participant(int id) {
        super(id);
    }

    public Participant(int id, String name, ParticipantType type)
    {
        super(id);
        this.name = name;
        this.type = type;
    }

    public boolean equals() {
        // TODO: implementieren
        return false;
    }

    public String getName()
    {
        return name;
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
