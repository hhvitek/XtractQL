package cz.hhvitek.xtractql.app.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cz.hhvitek.xtractql.app.service.api.XMLFetcherServiceTest;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class XMLFetcherFileDownloadServiceImplTest extends XMLFetcherServiceTest {

	@Autowired
	protected XMLFetcherFileDownloadServiceImplTest(XMLFetcherFileDownloadServiceImpl xmlFetcherService) {
		super(xmlFetcherService);
	}
}