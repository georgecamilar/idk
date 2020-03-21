package repositories;

import model.Arbiter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArbiterRepository extends AbstractRepo<Integer, Arbiter> {
    public ArbiterRepository(Properties props) {
        super(props);
    }

    @Override
    public Arbiter save(Arbiter arbiter) throws Exception {
        Connection connection = dbConnection.getConnection();
        String query = "insert into arbiter values (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, arbiter.getId());
            preparedStatement.setString(2, arbiter.getUsername());
            preparedStatement.setString(3, arbiter.getPassword());
            preparedStatement.setInt(4, arbiter.getProbaId());

            if (!preparedStatement.execute()) {
                throw new Exception("arbiter cannot be added!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbiter;
    }

    @Override
    public Arbiter find(Integer integer) {
        Connection connection = dbConnection.getConnection();
        Arbiter result = null;

        String query = "select * from arbiter where arbiter.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, integer);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Integer idProba = resultSet.getInt("idProba");

                result = new Arbiter(id, username, password, idProba);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    public Iterable<Arbiter> findAll() {
        Connection connection = dbConnection.getConnection();
        List<Arbiter> list = new ArrayList<>();
        String query = "select * from Arbiter";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                Integer id = results.getInt("id");
                String username = results.getString("username");
                String password = results.getString("password");
                Integer idProba = results.getInt("idProba");

                list.add(new Arbiter(id, username, password, idProba));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void deleteById(Integer integer) {
        Connection connection = dbConnection.getConnection();
        String query = "delete  from Arbiter where id=? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, integer);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Arbiter arbiter) {
        deleteById(arbiter.getId());
    }
}
