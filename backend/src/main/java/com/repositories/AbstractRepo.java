package com.repositories;

import com.model.HasId;
import com.utils.DatabaseConnection;

import java.util.Properties;

public abstract class AbstractRepo<Id, Entity extends HasId<Id>> implements MppRepository<Id, Entity> {

    protected DatabaseConnection dbConnection;

    public AbstractRepo(Properties props) {
        dbConnection = new DatabaseConnection(props);
    }
    
}
