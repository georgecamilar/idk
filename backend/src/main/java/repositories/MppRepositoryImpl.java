package repositories;

import model.HasId;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class MppRepositoryImpl<Id, Entity extends HasId> implements MppRepository<Id, Entity> {

    protected DatabaseConnection myConnection;
    protected String tableName;

    public MppRepositoryImpl(Properties props, String tableName) {
        this.myConnection = new DatabaseConnection(props);
        this.tableName = tableName;
    }

    @Override
    public Entity save(Entity entity) {
        Connection connection = myConnection.getConnection();

        try {
            preparedStatementString(entity, connection, "save");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Entity find(Id id) {
        Connection con = myConnection.getConnection();
        Entity response = null;
        try (PreparedStatement statement = con.prepareStatement("select * from "+tableName+" where id=?")) {
            statement.setString(1, this.tableName);
            statement.setString(2, id.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                response = contructEntity(set);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }


    @Override
    public Iterable<Entity> findAll() {
        List<Entity> resultList = new ArrayList<>();
        Connection connection = myConnection.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("select * from "+tableName)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultList.add(contructEntity(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public void deleteById(Id id) {
        Connection connection = myConnection.getConnection();
        try {
            preparedStatementString(id, connection, "delete");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Entity entity) {
        
    }

    protected abstract Entity contructEntity(ResultSet set) throws SQLException;

    protected abstract void preparedStatementString(Object parameter, Connection connection, String option) throws SQLException;

}

