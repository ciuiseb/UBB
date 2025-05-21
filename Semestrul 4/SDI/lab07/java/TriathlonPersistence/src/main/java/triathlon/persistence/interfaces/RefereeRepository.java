package triathlon.persistence.interfaces;


import triathlon.model.Referee;

import java.util.Optional;

public interface RefereeRepository extends Repository<Long, Referee>{
    Optional<Referee> findByUsername(String username);
}
