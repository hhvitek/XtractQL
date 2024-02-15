package cz.hhvitek.xtractql.app.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.jpa.entity.MunicipalityEntity;
import cz.hhvitek.xtractql.app.jpa.repository.MunicipalityRepository;
import cz.hhvitek.xtractql.app.service.api.MunicipalityService;


@Service
public class MunicipalityServiceImpl  implements MunicipalityService {
	private final MunicipalityRepository municipalityRepository;

	public MunicipalityServiceImpl(MunicipalityRepository municipalityRepository) {
		this.municipalityRepository = municipalityRepository;
	}

	@Override
	@Transactional
	public Municipality save(Municipality municipality) throws IOException {
		Assert.notNull(municipality, "Municipality cannot be null");

		MunicipalityEntity oldEntity = municipalityRepository.findById(municipality.getCode()).orElse(null);
		MunicipalityEntity entity = toEntity(municipality, oldEntity);
		// now here could be an additional step to convert "entity" back into "dto" but well lets just return "entity"
		return municipalityRepository.save(entity);
	}

	private MunicipalityEntity toEntity(Municipality newMunicipality, MunicipalityEntity oldEntity) {
		if (oldEntity != null) {
			return oldEntity.updateFrom(newMunicipality);
		}

		MunicipalityEntity newEntity = new MunicipalityEntity();
		return newEntity.updateFrom(newMunicipality);
	}
}
