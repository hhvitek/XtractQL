package cz.hhvitek.xtractql.app.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
	@JsonProperty("Obce")
	private List<Obec> obce = new ArrayList<>();
	@JsonProperty("CastiObci")
	private List<CastObce> castiObci = new ArrayList<>();

	/**
	 *
	 * @throws IndexOutOfBoundsException if obce is empty
	 */
	public Obec getFirstObec() {
		if (obecSize() == 0) {
			throw new IndexOutOfBoundsException();
		}

		return obce.get(0);
	}

	public int obecSize() {
		return obce.size();
	}

	public void calculateRelationships() {
		Map<String, Obec> uniqueIdToObec = obce.stream().
				collect(Collectors.toMap(Obec::getCode, Function.identity()));

		for (CastObce castObce: castiObci) {
			String obecCode = castObce.getMunicipality().getCode(); // that's the only field of Obec (inside castObce) set by xml parsing
			Obec obecByCode = uniqueIdToObec.get(obecCode);
			obecByCode.addMunicipalityPart(castObce);
			castObce.setMunicipality(obecByCode);
		}
	}
}
