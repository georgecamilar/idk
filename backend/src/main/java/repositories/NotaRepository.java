package repositories;

import model.Nota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class NotaRepository extends AbstractRepo<Integer, Nota> {

    public NotaRepository(Properties props) {
        super(props);
    }

    @Override
    public Nota save(Nota nota) throws Exception {
        
        
        Connection connection = dbConnection.getConnection();
        String query = "insert into nota values (?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1,this.getLastId()+1);
            preparedStatement.setInt(2, nota.getParticipantId());
            preparedStatement.setInt(3, nota.getProbaId());
            preparedStatement.setDouble(4, nota.getNota());


            if (!preparedStatement.execute()) {
                throw new Exception("Nota cannot be added");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return nota;
    }

    private int getLastId() {
        Connection connection = dbConnection.getConnection();
        try(PreparedStatement statement = connection.prepareStatement("select * from nota")){
            try(ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    result.last();
                    return result.getRow();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Nota find(Integer integer) {
        Nota result = null;

        Connection connection = dbConnection.getConnection();
        String query = "select * from nota where id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, integer);

            ResultSet set = preparedStatement.executeQuery();
            if (set.next()) {
                int id = set.getInt("id");
                int idParticipant = set.getInt("idParticipant");
                int idProba = set.getInt("idProba");
                double nota = set.getDouble("nota");

                result = new Nota(id, idParticipant, idProba, nota);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public Iterable<Nota> findAll() {
        List<Nota> list = new ArrayList<>();

        Connection connection = dbConnection.getConnection();
        String query = "select * from nota";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                list.add(new Nota(
                        set.getInt("id"),
                        set.getInt("idParticipant"),
                        set.getInt("idProba"),
                        set.getDouble("nota"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void deleteById(Integer integer) {
        String query = "delete from nota where id=?";
        Connection connection = dbConnection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, integer);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Nota nota) {
        deleteById(nota.getId());
    }

    public List<Nota> findByProbaId(Integer probaid) {
        List<Nota> list = new ArrayList<>();

        Connection connection = dbConnection.getConnection();
        String query = "select * from nota where idProba=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, probaid);

            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                int idParticipant = set.getInt("idParticipant");
                int idProba = set.getInt("idProba");
                double nota = set.getDouble("nota");

                list.add(new Nota(id, idParticipant, idProba, nota));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<Nota> sortByScore(List<Nota> list) {
        Collections.sort(list);
        return list;
    }

    public List<Nota> findByParticipantId(Integer participantId) {
        List<Nota> result = new ArrayList<>();

        Connection connection = dbConnection.getConnection();
        String query = "select * from nota where idParticipant=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, participantId);

            ResultSet set = preparedStatement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                int idParticipant = set.getInt("idParticipant");
                int idProba = set.getInt("idProba");
                double nota = set.getDouble("nota");

                result.add(new Nota(id, idParticipant, idProba, nota));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }


}
