package cz.hhvitek.xtractql.app.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
		if (containsAnyBlankObecKod()) {
			throw new IllegalArgumentException("XML contains Obec without proper identification");
		}
		if (containsAnyBlankCastObceKod()) {
			throw new IllegalArgumentException("XML contains CastObce without proper identification");
		}
		if (containsAnyBlankObecKodInsideCastObce()) {
			throw new IllegalArgumentException("XML contains CastObce without proper Obec identification");
		}

		Map<String, Obec> uniqueIdToObec = obce.stream()
				.collect(Collectors.toMap(Obec::getCode, Function.identity()));

		for (CastObce castObce: castiObci) {
			String obecId = castObce.getObec().getKod();
			if (!uniqueIdToObec.containsKey(obecId)) {
				throw new IllegalArgumentException("XML contains CastObce, without proper Obec found. CastObce Kod: " + castObce.getKod());
			}
			Obec obecById = uniqueIdToObec.get(obecId);
			obecById.addCastObce(castObce);
			castObce.setObec(obecById);
		}
	}

	private boolean containsAnyBlankObecKod() {
		return obce.stream()
				.map(Obec::getKod)
				.anyMatch(StringUtils::isBlank);
	}

	private boolean containsAnyBlankCastObceKod() {
		return castiObci.stream()
				.map(CastObce::getKod)
				.anyMatch(String::isBlank);
	}
	private boolean containsAnyBlankObecKodInsideCastObce() {
		return castiObci.stream()
				.map(CastObce::getObec)
				.map(Obec::getKod)
				.anyMatch(String::isBlank);
	}
}
