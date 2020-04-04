package controller;

import model.Arbiter;
import model.Nota;
import model.Participant;
import model.Proba;
import repositories.ArbiterRepository;
import repositories.NotaRepository;
import repositories.ParticipantRepository;
import repositories.ProbaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service {

    private ParticipantRepository participantRepository;
    private ArbiterRepository arbiterRepository;
    private ProbaRepository probaRepository;
    private NotaRepository notaRepository;

    public Service(Properties props) {
        participantRepository = new ParticipantRepository(props);
        arbiterRepository = new ArbiterRepository(props);
        probaRepository = new ProbaRepository(props);
        notaRepository = new NotaRepository(props);
    }

    public Integer login(String username, String password) {
        if (arbiterRepository.findByUsername(username).getPassword().equals(password)) {
            return arbiterRepository.findByUsername(username).getProbaId();
        }
        return -1;
    }


    public Arbiter saveArbiter(Arbiter arbiter) {
        try {
            return arbiterRepository.save(arbiter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void saveNota(int idParticipant, int idProba, Double grade) {
        try {
            this.notaRepository.save(new Nota(idParticipant, idProba, grade));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteArbiter(Integer id) {
        arbiterRepository.deleteById(id);
    }

    public Arbiter getArbiter(Integer id) {
        return this.arbiterRepository.find(id);
    }

    public Iterable<Arbiter> allArbiter() {
        return this.arbiterRepository.findAll();
    }

    public Proba saveProba(Proba proba) {
        try {
            return probaRepository.save(proba);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void deleteProba(Integer id) {
        this.probaRepository.deleteById(id);
    }

    public Proba getProba(Integer id) {
        return probaRepository.find(id);
    }

    public Iterable<Proba> allProba() {
        return probaRepository.findAll();
    }

    public Participant saveParticipant(Participant participant) {
        try {
            return participantRepository.save(participant);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void deleteParticipant(Integer id) {
        participantRepository.deleteById(id);
    }

    public Participant getParticipant(Integer id) {
        return participantRepository.find(id);
    }

    public Iterable<Participant> allParticipant() {
        return participantRepository.findAll();
    }

    public List<String> participantList() {
        List<String> result = new ArrayList<>();

        this.allParticipant().forEach(element -> {
            result.add(element.getName());
        });

        return result;
    }

    public Integer getIdParticipant(String name){
        try{
            return participantRepository.findId(name);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }
    
    public Nota saveNota(Nota nota) {
        try {
            return this.notaRepository.save(nota);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void deleteNota(Integer id) {
        notaRepository.deleteById(id);
    }

    public Nota getNota(Integer id) {
        return notaRepository.find(id);
    }

    public Iterable<Nota> allNota() {
        return notaRepository.findAll();
    }

    public List<Nota> filterNotaProba(Integer probaId) {
        List<Nota> result = new ArrayList<>();

        List<Nota> test = StreamSupport
                .stream(notaRepository.findAll().spliterator(), false)
                .filter(element -> element.getProbaId().equals(probaId))
                .collect(Collectors.toList());


        this.notaRepository.findAll().forEach(element -> {
            if (element.getProbaId().equals(probaId)) {
                result.add(element);
            }
        })
        ;
        return test;
    }

    public List<Participant> getAllTotalScores() {
        List<Participant> list = new ArrayList<>();
        for (Participant participant : participantRepository.findAll()) {
            Double grade = 0d;
            for (Nota nota : notaRepository.findByParticipantId(participant.getId())) {
                grade += nota.getNota();
            }
            participant.setTotal(grade);
            list.add(participant);
        }
        return list;
    }

    public List<Nota> getRaportList(Integer id) {
        return notaRepository.sortByScore(notaRepository.findByProbaId(id));
    }
}
