package repositories;

import model.Participant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantRepository extends AbstractRepo<Integer, Participant> {

    public ParticipantRepository(Properties props) {
        super(props);
    }

    @Override
    public Participant save(Participant participant) throws Exception {
        String query = "insert into participant values(?,?)";
        Connection connection = dbConnection.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, participant.getId());
            preparedStatement.setString(2, participant.getName());

            if (!preparedStatement.execute()) {
                throw new Exception("Cannot add participant to db");
            }
        }

        return participant;
    }

    @Override
    public Participant find(Integer integer) {
        String query = "select * from participant where id=?";
        Connection connection = dbConnection.getConnection();
        Participant participant = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, integer);

            ResultSet set = preparedStatement.executeQuery();
            if (set.next()) {
                participant = new Participant(set.getInt("id"), set.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return participant;
    }
    
    public Integer findId(String name){
        String query = "select * from participant where name=?";
        Connection connection = dbConnection.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                return set.getInt("id");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public Iterable<Participant> findAll() {
        List<Participant> list = new ArrayList<>();

        Connection connection = dbConnection.getConnection();
        String query = "select * from participant";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet set = preparedStatement.executeQuery();

            while (set.next()) {
                list.add(new Participant(set.getInt("id"), set.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void deleteById(Integer integer) {
        Connection connection = dbConnection.getConnection();
        String query = "delete from participant where id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, integer);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Participant participant) {
        deleteById(participant.getId());
    }
}
