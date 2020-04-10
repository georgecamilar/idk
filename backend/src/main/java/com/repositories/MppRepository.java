package com.repositories;

import com.model.HasId;

public interface MppRepository<Id, Entity extends HasId> {
    Entity save(Entity entity) throws Exception;

    Entity find(Id id);
    
    Iterable<Entity> findAll();
    
    void deleteById(Id id);
    
    void delete(Entity entity);
}
