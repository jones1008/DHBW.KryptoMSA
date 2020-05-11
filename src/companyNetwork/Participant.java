package companyNetwork;

public class Participant extends Subscriber {
    private String name;
    private ParticipantType type;

    public Participant(int id) {
        super(id);
    }

    public boolean equals() {
        // TODO: implementieren
        return false;
    }

//    @Subscribe
//    public void receive(FireEvent fireEvent) {
//        System.out.println("FireBrigade " + id);
//        System.out.println(fireEvent);
//        System.out.println();
//    }
}
