package cz.hhvitek.xtractql.app.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.service.api.XMLFetcherService;

@Service("xmlfetcher-onfly")
public class XMLFetcherJacksonOnFlyServiceImpl implements XMLFetcherService {

	@Override
	public Municipality fetch(URL url) throws IOException {
		Assert.notNull(url, "URL cannot be null");

		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(5 * 1000); // millis
		conn.setReadTimeout(60 * 1000);

		try (
				InputStream is = conn.getInputStream();
				ZipInputStream zis = new ZipInputStream(is)
		) {
			ZipEntry entry = zis.getNextEntry();
			if (entry == null) {
				throw new IllegalArgumentException("Unrecognized zip format. There is no file inside this archive. " + url);
			}

			if (entry.isDirectory()) {
				throw new IllegalArgumentException("There is a folder inside this archive. Not allowed. " + entry.getName());
			}

			XMLToPojoMapper mapper = new XMLToPojoMapper();
			return mapper.map(zis);
		}
	}
}
