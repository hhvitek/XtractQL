package cz.hhvitek.xtractql.app.xml;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VymennyFormat {
	@JsonProperty("Hlavicka")
	private Hlavicka hlavicka;
	private Data data;

	@JsonCreator
	public VymennyFormat(@JsonProperty(value = "Data", required = true) Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	public Hlavicka getHlavicka() {
		return hlavicka;
	}
}
