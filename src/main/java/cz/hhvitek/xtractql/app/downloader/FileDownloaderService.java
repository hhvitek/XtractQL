package cz.hhvitek.xtractql.app.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface FileDownloaderService {

	/**
	 * Let's fetch a file specified by URL and save this file into destinationFolder
	 *
	 * @throws IOException
	 * @throws IllegalArgumentException if either passed argument is null not a folder, ...
	 */
	File downloadFile(URL fileUrl, File destinationFolder) throws IOException;
}
