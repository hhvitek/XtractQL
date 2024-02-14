package cz.hhvitek.xtractql.app.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hlavicka {
	@JsonProperty("VerzeVFR")
	private String verzeVFR;

	public String getVerzeVFR() {
		return verzeVFR;
	}
}
