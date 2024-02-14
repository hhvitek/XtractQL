package cz.hhvitek.xtractql.app.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;

import cz.hhvitek.xtractql.app.Municipality;
import cz.hhvitek.xtractql.app.config.Properties;
import cz.hhvitek.xtractql.app.downloader.FileDownloaderService;
import cz.hhvitek.xtractql.app.service.api.XMLFetcherService;
import cz.hhvitek.xtractql.app.zip.ZipService;

@Service("xmlfetcher-download")
public class XMLFetcherFileDownloadServiceImpl implements XMLFetcherService {
	private final Properties properties;
	private final FileDownloaderService fileDownloaderService;
	private final ZipService zipService;

	public XMLFetcherFileDownloadServiceImpl(Properties properties, FileDownloaderService fileDownloaderService, ZipService zipService) {
		this.properties = properties;
		this.fileDownloaderService = fileDownloaderService;
		this.zipService = zipService;
	}

	/**
	 * Implements interface by proceeding step by step
	 * 1] download file 2] extract file 3] parses file using Jackson
	 */
	@Override
	public Municipality fetch(URL url) throws IOException {
		Assert.notNull(url, "URL cannot be null");

		// cleaning up after previously failed execution
		Path tmpFolder = Paths.get(properties.getTmpFolder());
		recreateEmptyFolder(tmpFolder);

		File downloadedFile = fileDownloaderService.downloadFile(url, tmpFolder.toFile());
		File unzippedFile = zipService.unzipSingle(downloadedFile, tmpFolder.toFile());
		Files.delete(downloadedFile.toPath()); // downloaded file is not required anymore

		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(unzippedFile))) {
			XMLToPojoMapper mapper = new XMLToPojoMapper();
			return mapper.map(bis);
		} finally {
			Files.delete(unzippedFile.toPath());
		}

	}

	private void recreateEmptyFolder(Path folderPath) throws IOException {
		FileSystemUtils.deleteRecursively(folderPath);
		Files.createDirectory(folderPath);
	}
}
