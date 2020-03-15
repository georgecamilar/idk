package run;

import controller.Controller;
import model.Arbiter;
import repositories.ArbiterRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Run {
    public static void main(String[] args){
        Properties properties = new Properties();
        try{
            properties.load(new FileReader("src/main/resources/bd.config"));

            Controller controller = new Controller(new ArbiterRepository(properties, "arbiter"));
            System.out.println("Created controller");
            
            controller.saveArbiter(new Arbiter(1, "mike","mike"));
            
            controller.findAllArbiter().forEach(e -> {
                System.out.printf("Arbiter %d %s %s",e.getId(),e.getUsername(),e.getPassword());
            });

            controller.deleteArbiterById(1);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
