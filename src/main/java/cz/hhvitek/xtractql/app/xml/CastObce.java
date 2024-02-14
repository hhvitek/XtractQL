package cz.hhvitek.xtractql.app.xml;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CastObce implements MunicipalityPart {
	private String code;
	@JsonProperty("Nazev")
	private String name;

	@JsonProperty("Obec")
	// field is se by parser, but only Kod field is set. Must be set manually from outside after parsing
	private Obec municipality;

	@JsonCreator
	public CastObce(@JsonProperty(value = "Kod", required = true) String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Municipality getMunicipality() {
		return municipality;
	}

	void setMunicipality(Obec municipality) {
		this.municipality = municipality;
	}
}
