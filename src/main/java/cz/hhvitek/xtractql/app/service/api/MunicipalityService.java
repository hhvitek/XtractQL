package cz.hhvitek.xtractql.app.service.api;

import java.io.IOException;

import cz.hhvitek.xtractql.app.Municipality;

public interface MunicipalityService {
	Municipality save(Municipality municipality) throws IOException;
}
