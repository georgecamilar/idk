package controller;

import model.Arbiter;
import repositories.ArbiterRepository;

public class Controller {
    private ArbiterRepository arbiterRepository;

    public Controller(ArbiterRepository arbiterRepository) {
        this.arbiterRepository = arbiterRepository;
    }

    public Arbiter saveArbiter(Arbiter arbiter) {
        try {
            return arbiterRepository.save(arbiter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Iterable<Arbiter> findAllArbiter(){
        return arbiterRepository.findAll();
    }
    
    public void deleteArbiterById(Integer id){
        arbiterRepository.deleteById(id);
    }

}
