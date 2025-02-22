package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.LinkedRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedRoleRepository extends MongoRepository<LinkedRole, String> {
}
