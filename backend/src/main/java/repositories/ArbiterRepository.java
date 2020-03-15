package repositories;

import model.Arbiter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ArbiterRepository extends MppRepositoryImpl<Integer, Arbiter> {
    public ArbiterRepository(Properties props, String tableName) {
        super(props, tableName);
    }

    @Override
    protected Arbiter contructEntity(ResultSet set) throws SQLException {
        int id = set.getInt("id");
        String username = set.getString("username");
        String password = set.getString("password");

        return new Arbiter(id, username, password);
    }

    @Override
    protected void preparedStatementString(Object parameter, Connection connection, String option) throws SQLException {
        switch (option) {
            case "save":
                Arbiter arbiter = (Arbiter) parameter;
                try (PreparedStatement statement = connection.prepareStatement("insert into "+tableName+" values(?, ?, ?)")) {
//                   statement.setString(1, tableName);
                    statement.setInt(1, arbiter.getId());
                    statement.setString(2, arbiter.getUsername());
                    statement.setString(3, arbiter.getPassword());
                    statement.execute();
                    break;
                }
            case "delete":
                Integer id = (Integer) parameter;
                try (PreparedStatement statement = connection.prepareStatement("delete from "+tableName+" where id = ?")) {
                    statement.setInt(1, id);
                    statement.execute();
                    break;
                }
            default:
                throw new SQLException("Command is not available");

        }
    }

}
