package cz.hhvitek.xtractql.app.zip;

import java.io.File;
import java.io.IOException;

public interface ZipService {
	/**
	 * Extract archive into destinationF folder
	 */
	void unzip(File zipFile, File destinationFolder) throws IOException;

	/**
	 * @param zipFile zip archive with only a single zipped file. Not a folder!
	 * @throws IOException error when working with archive
	 * @throws IllegalArgumentException if archive contains something else rather than a single file
	 * 									or if archive is not archive at all...
	 *
	 * @return unzipped File
	 */
	File unzipSingle(File zipFile, File destinationFolder) throws IOException;
}
