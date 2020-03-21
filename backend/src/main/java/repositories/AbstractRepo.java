package repositories;

import model.HasId;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractRepo<Id, Entity extends HasId<Id>> implements MppRepository<Id, Entity> {

    protected DatabaseConnection dbConnection;

    public AbstractRepo(Properties props) {
        dbConnection = new DatabaseConnection(props);
    }
    
}
