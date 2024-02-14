package cz.hhvitek.xtractql.app.jpa.entity;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "municipality_part")
public class MunicipalityPartEntity implements MunicipalityPart {
	@Id
	@Column(nullable = false, unique = true)
	private String code;
	@Column(nullable = false)
	private String name;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "municipality_id", nullable = false)
	private MunicipalityEntity municipality;

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Municipality getMunicipality() {
		return municipality;
	}

	public void setMunicipality(MunicipalityEntity municipalityEntity) {
		this.municipality = municipalityEntity;
	}
}
