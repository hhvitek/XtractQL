package cz.hhvitek.xtractql.app;

import java.util.List;

public interface Municipality extends Iterable<MunicipalityPart> {
	String getCode();
	String getName();

	List<MunicipalityPart> getMunicipalityParts();

	int size();
}
