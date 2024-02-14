package cz.hhvitek.xtractql.app.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ZipServiceImpl implements ZipService {
	@Override
	public void unzip(File zip, File destinationFolder) throws IOException {
		Assert.notNull(zip, "Passed File cannot be null");
		Assert.notNull(destinationFolder, "Passed File destinationFolder cannot be null");
		validateIsZipFile(zip);

		try (
				ZipInputStream zis = new ZipInputStream(new FileInputStream(zip))
		) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String sourceZippedFileName = entry.getName();
				Path targetUnzippedFileName = Path.of(destinationFolder.getAbsolutePath(), sourceZippedFileName);
				validateAgainstZipSlip(destinationFolder.toPath(), targetUnzippedFileName);

				if (entry.isDirectory()) {
					Files.createDirectories(targetUnzippedFileName);
				} else {
					extractFile(zis, targetUnzippedFileName.toFile());
				}
			}
		}
	}

	private void validateIsZipFile(File zip) throws IOException {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zip);
		} catch (ZipException e) {
			throw new IllegalArgumentException("Unrecognized zip format. " + e.getMessage());
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	/*
	 * https://security.snyk.io/research/zip-slip-vulnerability
	 */
	private void validateAgainstZipSlip(Path parentFolder, Path childFile) {
		if (!childFile.normalize().startsWith(parentFolder.normalize())) {
			throw new IllegalArgumentException("Archive contains illegal file name");
		}
	}

	private void extractFile(ZipInputStream zis, File targedUnzippedFile) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targedUnzippedFile))) {
			byte[] buffer = new byte[4096];
			int read = 0;
			while ((read = zis.read(buffer)) != -1) {
				bos.write(buffer, 0, read);
			}
		}
	}

	@Override
	public File unzipSingle(File zip, File destinationFolder) throws IOException {
		Assert.notNull(zip, "Passed File cannot be null");
		Assert.notNull(destinationFolder, "Passed File destinationFolder cannot be null");
		validateIsZipFile(zip);

		try (ZipFile zipFile = new ZipFile(zip)) {
			if (zipFile.size() != 1) {
				throw new IllegalArgumentException("There must be just a single file inside archive. No more, no less. " + zip.getAbsoluteFile());
			}

			ZipEntry entry = zipFile.entries().nextElement();
			if (entry.isDirectory()) {
				throw new IllegalArgumentException("There is a folder inside archive. Not allowed. " + zip.getAbsoluteFile());
			}

			unzip(zip, destinationFolder);
			return Path.of(destinationFolder.getAbsolutePath(), entry.getName()).toFile();
		}
	}


}
