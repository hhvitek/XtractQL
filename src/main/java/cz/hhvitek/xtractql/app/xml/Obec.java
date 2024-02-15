package cz.hhvitek.xtractql.app.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Obec implements Municipality {
	private String kod;
	@JsonProperty("Nazev")
	private String nazev;

	@JsonIgnore
	@JsonManagedReference
	// must be set from outside manually
	private List<CastObce> castObces = new ArrayList<>();

	@JsonCreator
	public Obec(@JsonProperty(value = "Kod",required = true) String kod) {
		this.kod = kod;
	}

	public Obec(String kod, String nazev) {
		this.kod = kod;
		this.nazev = nazev;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getNazev() {
		return nazev;
	}

	public void setNazev(String nazev) {
		this.nazev = nazev;
	}

	public List<CastObce> getCastObces() {
		return castObces;
	}

	public void setCastObces(List<CastObce> castObces) {
		this.castObces = castObces;
	}

	@Override
	public String getCode() {
		return kod;
	}

	@Override
	public String getName() {
		return nazev;
	}

	@Override
	public List<MunicipalityPart> getMunicipalityParts() {
		return Collections.unmodifiableList(castObces);
	}

	@Override
	public int size() {
		return castObces.size();
	}

	@Override
	public Iterator<MunicipalityPart> iterator() {
		return new ArrayList<MunicipalityPart>(castObces).iterator();
	}

	public void addCastObce(CastObce castObce) {
		castObces.add(castObce);
	}
}
