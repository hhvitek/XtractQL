package cz.hhvitek.xtractql.app.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cz.hhvitek.xtractql.TestUtils;
import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

@ExtendWith(SpringExtension.class)
public class XMLToPojoMapperTest {
	private XMLToPojoMapper mapper = new XMLToPojoMapper();

	@Test
	public void parseTest() throws IOException {
		String xml = TestUtils.getSimpleVymennyFormatXML();
		Municipality municipality;
		try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			municipality = mapper.map(is);
		}

		TestUtils.assertExpectedSimpleVymennyFormat_Municipality(municipality);
	}

	@Test
	void parseInvalidXmlTest() throws IOException {
		String invalidXml = "invalid xml input";
		try (InputStream is = new ByteArrayInputStream(invalidXml.getBytes(StandardCharsets.UTF_8))) {
			IOException ex = Assertions.assertThrows(IOException.class, () -> mapper.map(is));
			Assertions.assertTrue(ex.getMessage().startsWith("Unexpected character \'i\'"));
		}
	}

	@Test
	void parseNoMunicipalityXmlTest() throws IOException {
		String xml = """
			<?xml version="1.0" encoding="UTF-8"?>
			<vf:VymennyFormat
					xmlns:gml="http://www.opengis.net/gml/3.2"
					xmlns:vf="urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1"
					xmlns:obi="urn:cz:isvs:ruian:schemas:ObecIntTypy:v1"
					xmlns:coi="urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1"
			>
				<vf:Data>
					<vf:CastiObci>
						<vf:CastObce gml:id="CO.31801">
							<coi:Kod>31801</coi:Kod>
							<coi:Nazev>Drahoraz</coi:Nazev>
							<coi:Obec>
								<obi:Kod>573060</obi:Kod>
							</coi:Obec>
						</vf:CastObce>
						<vf:CastObce gml:id="CO.31828">
							<coi:Kod>31828</coi:Kod>
							<coi:Nazev>Pševes</coi:Nazev>
							<coi:Obec>
								<obi:Kod>573060</obi:Kod>
							</coi:Obec>
						</vf:CastObce>
					</vf:CastiObci>
				</vf:Data>
			</vf:VymennyFormat>
				""";

		try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.map(is));
			Assertions.assertTrue(ex.getMessage().contains("Expecting a single Municipality."));
		}
	}

	@Test
	void parseMissingRequiredFieldsXmlTest() throws IOException {
		String xmlObecWithoutCode = """
			<?xml version="1.0" encoding="UTF-8"?>
			<vf:VymennyFormat
					xmlns:gml="http://www.opengis.net/gml/3.2"
					xmlns:vf="urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1"
					xmlns:obi="urn:cz:isvs:ruian:schemas:ObecIntTypy:v1"
					xmlns:coi="urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1"
			>
				<vf:Hlavicka>
					<vf:VerzeVFR>3.1</vf:VerzeVFR>
				</vf:Hlavicka>
				<vf:Data>
					<vf:Obce>
						<vf:Obec gml:id="OB.573060">
							<obi:Nazev>Kopidlno</obi:Nazev>
						</vf:Obec>
					</vf:Obce>
					<vf:CastiObci>
						<vf:CastObce gml:id="CO.31801">
							<coi:Kod>31801</coi:Kod>
							<coi:Nazev>Drahoraz</coi:Nazev>
							<coi:Obec>
								<obi:Kod>573060</obi:Kod>
							</coi:Obec>
						</vf:CastObce>
						<vf:CastObce gml:id="CO.31828">
							<coi:Kod>31828</coi:Kod>
							<coi:Nazev>Pševes</coi:Nazev>
							<coi:Obec>
								<obi:Kod>573060</obi:Kod>
							</coi:Obec>
						</vf:CastObce>
					</vf:CastiObci>
				</vf:Data>
			</vf:VymennyFormat>
				""";

		try (InputStream is = new ByteArrayInputStream(xmlObecWithoutCode.getBytes(StandardCharsets.UTF_8))) {
			IOException ex = Assertions.assertThrows(IOException.class, () -> mapper.map(is));
			Assertions.assertTrue(ex.getMessage().startsWith("Missing required creator property 'Kod'"));
		}
	}

	@Test
	void parseMissingRequiredFields_NoObecInsideCastObce_XmlTest() throws IOException {
		String xml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<vf:VymennyFormat
						xmlns:gml="http://www.opengis.net/gml/3.2"
						xmlns:vf="urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1"
						xmlns:obi="urn:cz:isvs:ruian:schemas:ObecIntTypy:v1"
						xmlns:coi="urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1"
				>
					<vf:Hlavicka>
						<vf:VerzeVFR>3.1</vf:VerzeVFR>
					</vf:Hlavicka>
					<vf:Data>
						<vf:Obce>
							<vf:Obec gml:id="OB.573060">
								<obi:Kod>573060</obi:Kod>
								<obi:Nazev>Kopidlno</obi:Nazev>
							</vf:Obec>
						</vf:Obce>
						<vf:CastiObci>
							<vf:CastObce gml:id="CO.31801">
								<coi:Kod>31801</coi:Kod>
								<coi:Nazev>Drahoraz</coi:Nazev>
							</vf:CastObce>
							<vf:CastObce gml:id="CO.31828">
								<coi:Kod>31828</coi:Kod>
								<coi:Nazev>Pševes</coi:Nazev>
								<coi:Obec>
									<obi:Kod>573060</obi:Kod>
								</coi:Obec>
							</vf:CastObce>
						</vf:CastiObci>
					</vf:Data>
				</vf:VymennyFormat>
				""";

		try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			IOException ex = Assertions.assertThrows(IOException.class, () -> mapper.map(is));
			Assertions.assertTrue(ex.getMessage().startsWith("Missing required creator property 'Obec'"));
		}
	}

	@Test
	void parseMissingRequiredFields_NoObecInsideCastObce_InvalidIdTest() throws IOException {
		String xml = """
				<?xml version="1.0" encoding="UTF-8"?>
				<vf:VymennyFormat
						xmlns:gml="http://www.opengis.net/gml/3.2"
						xmlns:vf="urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1"
						xmlns:obi="urn:cz:isvs:ruian:schemas:ObecIntTypy:v1"
						xmlns:coi="urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1"
				>
					<vf:Hlavicka>
						<vf:VerzeVFR>3.1</vf:VerzeVFR>
					</vf:Hlavicka>
					<vf:Data>
						<vf:Obce>
							<vf:Obec gml:id="OB.573060">
								<obi:Kod>573060</obi:Kod>
								<obi:Nazev>Kopidlno</obi:Nazev>
							</vf:Obec>
						</vf:Obce>
						<vf:CastiObci>
							<vf:CastObce gml:id="CO.31801">
								<coi:Kod>31801</coi:Kod>
								<coi:Nazev>Drahoraz</coi:Nazev>
								<coi:Obec>
									<obi:Kod>666666</obi:Kod>
								</coi:Obec>
							</vf:CastObce>
							<vf:CastObce gml:id="CO.31828">
								<coi:Kod>31828</coi:Kod>
								<coi:Nazev>Pševes</coi:Nazev>
								<coi:Obec>
									<obi:Kod>573060</obi:Kod>
								</coi:Obec>
							</vf:CastObce>
						</vf:CastiObci>
					</vf:Data>
				</vf:VymennyFormat>
				""";

		try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
			IllegalArgumentException ex = Assertions.assertThrows(IllegalArgumentException.class, () -> mapper.map(is));
			Assertions.assertEquals("XML contains CastObce, without proper Obec found. CastObce Kod: 31801", ex.getMessage());
		}
	}

	@Test
	public void parseValidXmlFile15MBInSize() throws IOException {
		File originalExampleXml = new ClassPathResource("20210331_OB_573060_UZSZ.xml").getFile();
		Assertions.assertNotNull(originalExampleXml);
		Assertions.assertTrue(originalExampleXml.exists());

		Municipality municipality;
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(originalExampleXml))) {
			municipality = mapper.map(bis);
		}

		assertKopidlnoMunicipality(municipality, 5);
		Assertions.assertEquals(5, municipality.size());
		for (MunicipalityPart mPart: municipality) {
			switch (mPart.getCode()) {
				case "31801":
					Assertions.assertEquals("Drahoraz", mPart.getName());
					assertKopidlnoMunicipality(mPart.getMunicipality(), 5);
					break;
				case "31828":
					Assertions.assertEquals("Pševes", mPart.getName());
					assertKopidlnoMunicipality(mPart.getMunicipality(), 5);
					break;
				default:
					break;
			}
		}
	}

	private void assertKopidlnoMunicipality(Municipality municipality, int expectedSize) {
		Assertions.assertNotNull(municipality);
		Assertions.assertEquals("Kopidlno", municipality.getName());
		Assertions.assertEquals("573060", municipality.getCode());
		Assertions.assertEquals(expectedSize, municipality.size());
	}
}