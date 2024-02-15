package cz.hhvitek.xtractql.app.jpa.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	private List<MunicipalityPartEntity> mPartEntities = new ArrayList<>();

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

	public List<MunicipalityPartEntity> getMPartEntities() {
		return mPartEntities;
	}

	public void setMPartEntities(List<MunicipalityPartEntity> mPartEntities) {
		this.mPartEntities = mPartEntities;
	}

	@Override
	public List<MunicipalityPart> getMunicipalityParts() {
		return Collections.unmodifiableList(mPartEntities);
	}

	@Override
	public int size() {
		return mPartEntities.size();
	}

	@Override
	public Iterator<MunicipalityPart> iterator() {
		return new ArrayList<MunicipalityPart>(mPartEntities).iterator();
	}

	public MunicipalityEntity updateFrom(Municipality newMunicipality) {
		if (newMunicipality.getCode() != null) {
			code = newMunicipality.getCode();
		}
		if (newMunicipality.getName() != null) {
			name = newMunicipality.getName();
		}
		mPartEntities.clear();
		for (MunicipalityPart mPart: newMunicipality) {
			validateMunicipalityPart(mPart);
			MunicipalityPartEntity entity = new MunicipalityPartEntity(mPart.getCode(), mPart.getName(), this);
			mPartEntities.add(entity);
		}

		return this;
	}

	private void validateMunicipalityPart(MunicipalityPart mPart) {
		if (mPart == null || mPart.getMunicipality() == null || !StringUtils.equals(code, mPart.getMunicipality().getCode())) {
			throw new IllegalArgumentException("Inconsistent state. Municipality code does not match municipality code present inside municipality part");
		}
	}
}
