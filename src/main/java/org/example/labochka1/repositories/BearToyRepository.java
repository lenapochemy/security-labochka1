package org.example.labochka1.repositories;

import org.example.labochka1.model.BearToy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BearToyRepository extends CrudRepository<BearToy, Long> {

}
