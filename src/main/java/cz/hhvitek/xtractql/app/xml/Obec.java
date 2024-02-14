package cz.hhvitek.xtractql.app.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Obec implements Municipality {
	private String code;
	@JsonProperty("Nazev")
	private String name;

	@JsonCreator
	public Obec(@JsonProperty(value = "Kod",required = true) String code) {
		this.code = code;
	}

	@JsonIgnore
	// must be set from outside manually
	private List<CastObce> municipalityParts = new ArrayList<>();

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<MunicipalityPart> getMunicipalityParts() {
		return Collections.unmodifiableList(municipalityParts);
	}

	@Override
	public int size() {
		return municipalityParts.size();
	}

	@Override
	public Iterator<MunicipalityPart> iterator() {
		return new ArrayList<MunicipalityPart>(municipalityParts).iterator();
	}

	void addMunicipalityPart(CastObce municipalityPart) {
		municipalityParts.add(municipalityPart);
	}

}
