package cz.hhvitek.xtractql.app.service.impl;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import cz.hhvitek.xtractql.app.jpa.repository.MunicipalityRepository;
import cz.hhvitek.xtractql.app.service.api.MunicipalityService;
import cz.hhvitek.xtractql.app.xml.CastObce;
import cz.hhvitek.xtractql.app.xml.Obec;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // because we don't use h2
class MunicipalityServiceImplTest {
	@Autowired
	private MunicipalityRepository repository;

	@Test
	public void doesMapMapperWorksBasic() throws IOException {
		String obecCode = "obecCode";
		String obecName = "obecName";
		String castObceCode = "obecPartCode";
		String castObceName = "obecPartName";

		Obec obec = new Obec(obecCode, obecName);
		CastObce castObce = new CastObce(castObceCode, castObceName, obec);
		obec.addCastObce(castObce);

		MunicipalityService service = new MunicipalityServiceImpl(repository);
		service.save(obec);
		service.save(obec); // basically no exception invoked is a test here
	}

}