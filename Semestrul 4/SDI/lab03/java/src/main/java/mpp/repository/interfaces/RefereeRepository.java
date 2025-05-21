package mpp.repository.interfaces;


import mpp.domain.Referee;

import java.util.Optional;

public interface RefereeRepository extends Repository<Long, Referee>{
    Optional<Referee> findByUsername(String username);
}
