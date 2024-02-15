package cz.hhvitek.xtractql.app.xml;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import cz.hhvitek.xtractql.app.MunicipalityPart;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CastObce implements MunicipalityPart {
	private String kod;
	@JsonProperty("Nazev")
	private String nazev;

	@JsonBackReference
	// field is se by parser, but only Kod field is set. Must be set manually from outside after parsing
	private Obec obec;

	@JsonCreator
	public CastObce(@JsonProperty(value = "Kod", required = true) String kod, @JsonProperty(value = "Obec", required = true) Obec obec) {
		this.kod = kod;
		this.obec = obec;
	}

	public CastObce(String kod, String nazev, Obec obec) {
		this.kod = kod;
		this.nazev = nazev;
		this.obec = obec;
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

	public Obec getObec() {
		return obec;
	}

	public void setObec(Obec obec) {
		this.obec = obec;
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
	public Obec getMunicipality() {
		return obec;
	}


}
