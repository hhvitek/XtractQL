package cz.hhvitek.xtractql.app.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.hhvitek.xtractql.app.jpa.entity.MunicipalityEntity;

@Repository
public interface MunicipalityRepository extends JpaRepository<MunicipalityEntity, String> {
}
