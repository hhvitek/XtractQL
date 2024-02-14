package cz.hhvitek.xtractql.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@PropertySource("classpath:application.properties")
@Getter
public class Properties {
	@Value("${parser.file.maxsize}")
	private int maxFileSizeInMB;

	@Value("${parser.tmp.folder}")
	private String tmpFolder;
}
