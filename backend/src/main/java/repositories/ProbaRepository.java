package repositories;

import model.Proba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProbaRepository extends AbstractRepo<Integer, Proba> {
    public ProbaRepository(Properties props) {
        super(props);
    }

    @Override
    public Proba save(Proba proba) throws Exception {
        String query = "insert into proba values(?,?)";
        Connection connection = dbConnection.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, proba.getId());
            statement.setInt(2, proba.getArbiterId());

            if (!statement.execute()) {
                throw new Exception("Cannot add proba");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return proba;
    }

    @Override
    public Proba find(Integer integer) {
        String query = "select * from proba where id=?";
        Connection connection = dbConnection.getConnection();
        Proba result = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, integer);
            ResultSet set = statement.executeQuery();
            if (set.next()) {

                result = new Proba(integer, set.getInt("idArbiter"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Iterable<Proba> findAll() {
        List<Proba> list = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "select * from proba";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                list.add(new Proba(set.getInt("id"), set.getInt("idArbiter")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void deleteById(Integer integer) {
        String query = "delete from proba where id=?";
        Connection connection = dbConnection.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, integer);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Proba proba) {
        deleteById(proba.getId());
    }
}
