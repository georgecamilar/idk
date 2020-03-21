package controller;

import repositories.ArbiterRepository;
import repositories.NotaRepository;
import repositories.ParticipantRepository;
import repositories.ProbaRepository;

import java.util.Properties;

public class Controller {

    private ParticipantRepository participantRepository;
    private ArbiterRepository arbiterRepository;
    private ProbaRepository probaRepository;
    private NotaRepository notaRepository;

    public Controller(Properties props) {
        participantRepository = new ParticipantRepository(props);
        arbiterRepository = new ArbiterRepository(props);
        probaRepository = new ProbaRepository(props);
        notaRepository = new NotaRepository(props);
    }


    public ParticipantRepository getParticipantRepository() {
        return participantRepository;
    }

    public ArbiterRepository getArbiterRepository() {
        return arbiterRepository;
    }

    public ProbaRepository getProbaRepository() {
        return probaRepository;
    }

    public NotaRepository getNotaRepository() {
        return notaRepository;
    }
}
