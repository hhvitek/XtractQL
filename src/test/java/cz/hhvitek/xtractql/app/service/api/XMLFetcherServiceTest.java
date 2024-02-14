package cz.hhvitek.xtractql.app.service.api;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import cz.hhvitek.xtractql.TestUtils;
import cz.hhvitek.xtractql.app.Municipality;

public abstract class XMLFetcherServiceTest {
	protected XMLFetcherService xmlFetcherService;

	protected XMLFetcherServiceTest(XMLFetcherService xmlFetcherService) {
		this.xmlFetcherService = xmlFetcherService;
	}

	@Test
	public void parseTest() throws IOException {
		URL xmlFile = new ClassPathResource("vymenny_format_file.zip").getURL();
		Municipality municipality = xmlFetcherService.fetch(xmlFile);
		TestUtils.assertExpectedSimpleVymennyFormat_Municipality(municipality);
	}

	@Test
	public void parseXmlFileDirectly_FailsTest() throws IOException {
		URL invalidFile = new ClassPathResource("vymenny_format_file.xml").getURL();
		IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> xmlFetcherService.fetch(invalidFile));
		Assertions.assertTrue(ex.getMessage().contains("Unrecognized zip format."));
	}

}