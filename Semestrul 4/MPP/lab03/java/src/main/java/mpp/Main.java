package mpp;


public class Main {
    public static void main(String[] args) {
        var evnetrepository = new mpp.repository.impl.EventRepositoryImpl();
        var participantrepository = new mpp.repository.impl.ParticipantRepositoryImpl();
        var resultrepository = new mpp.repository.impl.ResultRepositoryImpl(evnetrepository, participantrepository);

        System.out.println(participantrepository.findAll());
    }
}