package cz.hhvitek.xtractql.app.jpa.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "municipality")
public class MunicipalityEntity implements Municipality {
	@Id
	@Column(nullable = false, unique = true)
	private String code;
	@Column(nullable = false)
	private String name;

	@OneToMany(mappedBy = "municipality", cascade = CascadeType.ALL)
	private List<MunicipalityPartEntity> municipalityParts = new ArrayList<>();

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

	@Override
	public List<MunicipalityPart> getMunicipalityParts() {
		return Collections.unmodifiableList(municipalityParts);
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int size() {
		return municipalityParts.size();
	}

	public void setMunicipalityParts(List<MunicipalityPartEntity> municipalityPartEntities) {
		this.municipalityParts = municipalityPartEntities;
	}

	@Override
	public Iterator<MunicipalityPart> iterator() {
		return new ArrayList<MunicipalityPart>(municipalityParts).iterator();
	}
}
