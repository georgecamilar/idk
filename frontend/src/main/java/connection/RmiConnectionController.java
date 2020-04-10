package connection;

import com.model.Nota;
import com.model.Participant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class RmiConnectionController {
    private ApplicationContext factory;
    private Service service;

    public RmiConnectionController() {
        factory = new ClassPathXmlApplicationContext("classpath:client.xml");
        service = (Service) factory.getBean("myService");
    }

    public void addNota(Nota nota) {
        service.saveNota(nota);
    }

    public List<Participant> getAllScores() {
        return service.getAllTotalScores();
    }

    public Integer login(String username, String password) {
        return service.login(username, password);
    }

    public List<Nota> getReport(Integer id) {
        return service.getRaportList(id);
    }
    
}
