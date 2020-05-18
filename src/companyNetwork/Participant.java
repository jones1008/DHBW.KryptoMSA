package companyNetwork;

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

//    @Subscribe
//    public void receive(FireEvent fireEvent) {
//        System.out.println("FireBrigade " + id);
//        System.out.println(fireEvent);
//        System.out.println();
//    }
}
