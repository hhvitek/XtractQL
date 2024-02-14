package cz.hhvitek.xtractql;

import org.junit.jupiter.api.Assertions;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.MunicipalityPart;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {
	public static String getSimpleVymennyFormatXML() {
		return """
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
	}

	public static void assertExpectedSimpleVymennyFormat_Municipality(Municipality municipality) {
		assertKopidlnoMunicipality(municipality, 2);

		for (MunicipalityPart mPart: municipality) {
			switch (mPart.getCode()) {
				case "31801":
					Assertions.assertEquals("Drahoraz",mPart.getName());
					assertKopidlnoMunicipality(mPart.getMunicipality(), 2);
					break;
				case "31828":
					Assertions.assertEquals("Pševes",mPart.getName());
					assertKopidlnoMunicipality(mPart.getMunicipality(), 2);
					break;
				default:
					Assertions.fail("Unknown MunicipalityPart: " + mPart);
					break;
			}
		}
	}

	private static void assertKopidlnoMunicipality(Municipality municipality, int expectedSize) {
		Assertions.assertNotNull(municipality);
		Assertions.assertEquals("Kopidlno", municipality.getName());
		Assertions.assertEquals("573060", municipality.getCode());
		Assertions.assertEquals(expectedSize, municipality.size());
	}
}
