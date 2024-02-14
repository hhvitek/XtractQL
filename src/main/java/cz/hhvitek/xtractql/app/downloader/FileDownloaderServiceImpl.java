package cz.hhvitek.xtractql.app.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cz.hhvitek.xtractql.app.config.Properties;

@Service
public class FileDownloaderServiceImpl implements FileDownloaderService {
	private static final int BUFFER_SIZE_IN_BYTES = 4096;
	private static int MAX_FILE_SIZE_IN_BYTES;

	@Autowired
	public FileDownloaderServiceImpl(Properties properties) {
		MAX_FILE_SIZE_IN_BYTES = properties.getMaxFileSizeInMB() * 1_000_000;
	}

	FileDownloaderServiceImpl(int maxFileSizeInBytes) {
		MAX_FILE_SIZE_IN_BYTES = maxFileSizeInBytes;
	}

	@Override
	public File downloadFile(URL fileUrl, File destinationFolder) throws IOException {
		Assert.notNull(fileUrl, "Passed file URL cannot be null");
		Assert.notNull(destinationFolder, "Passed destinationFolder cannot be null");

		if (!destinationFolder.exists()) {
			throw new IllegalArgumentException("File does not exist: " + destinationFolder.getAbsoluteFile());
		}

		if (!destinationFolder.isDirectory()) {
			throw new IllegalArgumentException(("Destination folder File is not actually a folder. " + destinationFolder.getAbsoluteFile()));
		}

		// Extract file name from URL
		String fileName = fileUrl.toString().substring(fileUrl.toString().lastIndexOf('/') + 1);
		if (fileName.contains("..") || ".".equals(fileName) || StringUtils.isBlank(fileName)) {
			throw new IllegalArgumentException("I dont like this kind of fileName: \"" + fileName + "\"");
		}

		// Construct path for downloaded file
		Path destinationFile = Paths.get(destinationFolder.getAbsolutePath(), fileName);

		Files.createFile(destinationFile);
		return downloadFileFromURL(fileUrl, destinationFile.toFile());
 	}

	private File downloadFileFromURL(URL fileUrl, File destinationFile) throws IOException {
		try (
				BufferedInputStream in = new BufferedInputStream(fileUrl.openStream());
				FileOutputStream out = new FileOutputStream(destinationFile)
		) {
			byte[] buffer = new byte[BUFFER_SIZE_IN_BYTES];
			int bytesRead;
			int totalBytesRead = 0;

			while ((bytesRead = in.read(buffer, 0, BUFFER_SIZE_IN_BYTES)) != -1) {
				out.write(buffer, 0, bytesRead);

				totalBytesRead += bytesRead;
				if (totalBytesRead > MAX_FILE_SIZE_IN_BYTES) {
					throw new IOException("File exceeds the maximum allowed file size.");
				}
			}
		}

		return destinationFile;
	}
}
