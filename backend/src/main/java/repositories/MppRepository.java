package repositories;

import model.HasId;

public interface MppRepository<Id, Entity extends HasId> {
    Entity save(Entity entity);

    Entity find(Id id);
    
    Iterable<Entity> findAll();
    
    void deleteById(Id id);
    
    void delete(Entity entity);
}
