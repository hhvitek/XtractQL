package cz.hhvitek.xtractql.app.downloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

@ExtendWith(SpringExtension.class)
class FileDownloaderServiceTest {
	private FileDownloaderService fileDownloaderService = new FileDownloaderServiceImpl(1000);

	private static File testFolderInsideSystemTemp;

	@BeforeAll
	public static void setUp() throws IOException {
		Path targetTmpFolder = Path.of(System.getProperty("java.io.tmpdir"), "TEST_TMP_FOLDER_PREFIX_");
		FileSystemUtils.deleteRecursively(targetTmpFolder);
		testFolderInsideSystemTemp = Files.createDirectory(targetTmpFolder).toFile();
	}

	@AfterAll
	public static void tearDown() throws IOException {
		FileSystemUtils.deleteRecursively(testFolderInsideSystemTemp.toPath());
	}

	@Test
	public void dontAllowFile_onlyDirectoriesAreAllowedTest() throws Exception {
		URL httpURL = new URL("http:///home.user/file.xml");
		Path tmpFile = Files.createTempFile(testFolderInsideSystemTemp.toPath(), "prefix_", "_suffix");
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> fileDownloaderService.downloadFile(httpURL, tmpFile.toFile()));
	}

	@Test
	public void dontAllowNonExistentFolderTest() throws Exception {
		URL httpURL = new URL("http:///home.user/file.xml");
		File nonExistentFolder = new File("/hello/world/x/t/g");
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> fileDownloaderService.downloadFile(httpURL, nonExistentFolder));
	}

	@Test
	public void dontAllowWeirdUrlFileNameTest() throws Exception {
		URL weirdUrl = new URL("http://localhost/1/2/3/4/..");
		Assertions.assertThrows(IllegalArgumentException.class,
				() -> fileDownloaderService.downloadFile(weirdUrl, testFolderInsideSystemTemp));
	}

	@Test
	public void tryToDownloadMockedURL() throws IOException {
		URL vymennyFormatFileUrl = new ClassPathResource("vymenny_format_file.xml").getURL();
		File downloadedFile = fileDownloaderService.downloadFile(vymennyFormatFileUrl, testFolderInsideSystemTemp);
		Assertions.assertTrue(downloadedFile.exists());
	}

}