package cz.hhvitek.xtractql.app.jpa.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import cz.hhvitek.xtractql.app.jpa.entity.MunicipalityPartEntity;

@Repository
public interface MunicipalityPartRepository extends ListCrudRepository<MunicipalityPartEntity, String> {
}
