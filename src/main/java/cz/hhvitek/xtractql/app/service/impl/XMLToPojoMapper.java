package cz.hhvitek.xtractql.app.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.xml.VymennyFormat;

public class XMLToPojoMapper {

	public Municipality map(InputStream xmlInputStream) throws IOException {
		Assert.notNull(xmlInputStream, "Passed xml stream cannot be null");

		ObjectMapper om = new XmlMapper();

		VymennyFormat vymennyFormat = om.readValue(xmlInputStream, VymennyFormat.class);
		if (vymennyFormat.getData().obecSize() != 1) {
			throw new IllegalArgumentException("Unexpected XML format. Expecting a single Municipality. But " + vymennyFormat.getData().obecSize() + " found.");
		}

		vymennyFormat.getData().calculateRelationships();
		return vymennyFormat.getData().getFirstObec();
	}
}
