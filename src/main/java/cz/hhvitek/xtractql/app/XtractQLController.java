package cz.hhvitek.xtractql.app;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cz.hhvitek.xtractql.app.service.api.MunicipalityService;
import cz.hhvitek.xtractql.app.service.api.XMLFetcherService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(XtractQLController.BASE_PATH)
@Slf4j
public class XtractQLController {
	public static final String BASE_PATH = "/xtractql";

	private final XMLFetcherService XMLFetcherService;
	private final MunicipalityService municipalityService;

	public XtractQLController(@Qualifier("xmlfetcher-onfly") XMLFetcherService XMLFetcherService, MunicipalityService municipalityService) {
		this.XMLFetcherService = XMLFetcherService;
		this.municipalityService = municipalityService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse welcomePage() {
		log.info("Welcome page invoked");
		return ApiResponse.ok()
				.message("It's working! Use endpoint POST { fileURL }");
	}

	@PostMapping
	public ResponseEntity<ApiResponse> parseXml(@RequestParam String fileUrl) throws IOException {
		log.info("Starting to process xml file: {}", fileUrl);

		URL url = parseUrl(fileUrl);
		if (url == null) {
			return ResponseEntity.badRequest()
					.body(
							ApiResponse.badRequest()
									.message("Passed Invalid URL")
					);
		}

		if (!url.getProtocol().startsWith("http")) {
			return ResponseEntity.badRequest()
					.body(
							ApiResponse.badRequest()
									.message("Let's not go there. Just simple http")
					);
		}

		if (!isUrlAvailable(url)) {
			return ResponseEntity.badRequest()
					.body(
							ApiResponse.badRequest()
									.message("Given file is unreachable. " + url)
					);
		}

		Municipality municipality = XMLFetcherService.fetch(url);
		municipality = municipalityService.save(municipality);
		return ResponseEntity.ok(ApiResponse.ok().message(municipality.toString()));
	}

	private URL parseUrl(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private boolean isUrlAvailable(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		try {
			HttpURLConnection.setFollowRedirects(false);
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);

			conn.setRequestMethod(HttpMethod.HEAD.name());
			int responseCode = conn.getResponseCode();
			return responseCode == HttpStatus.OK.value();
		} catch (Exception ex) {
			return false;
		} finally {
			conn.disconnect();
		}
	}


}
