package cz.hhvitek.xtractql.app.service.api;

import java.io.IOException;
import java.net.URL;

import cz.hhvitek.xtractql.app.Municipality;

public interface XMLFetcherService {
	/**
	 * Fetch a file represented by URL parameter. This file is a single ZIP archived XML file.
	 * Uncompress this archive, parse this XML file and fill POJO class implementing Municipality interface.
	 *
	 * @param url File to fetch
	 * @return interface representing downloaded XML file (POJO)
	 * @throws IOException
	 * @throws IllegalArgumentException if passed URL is null, archive/xml file is not exactly as specified
	 */
	Municipality fetch(URL url) throws IOException;
}
