package cz.hhvitek.xtractql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import cz.hhvitek.xtractql.app.XtractQLController;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class XtractQLApplicationTests {
	private static final String BASE_API_PATH = XtractQLController.BASE_PATH;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void basicIntegrationWelcomePageTest() throws Exception {
		mockMvc.perform(
				get(BASE_API_PATH)
						.accept(MediaType.APPLICATION_JSON_VALUE)
		)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(3)))
				.andExpect(jsonPath("status", is(200)));
	}

	@Test
	void basicIntegrationNonExistentPageTest() throws Exception {
		mockMvc.perform(
						get(BASE_API_PATH + "/non_existent_endpoint")
								.header("Host", "localhost:8080")
								.accept(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isNotFound());
	}

	@Test
	void basicPostOtherThanHttpUrlNotAllowedTest() throws Exception {
		mockMvc.perform(
						post(BASE_API_PATH)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.param("fileUrl", "file:///home/user/data.xml.zip")
								.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("status", is(400)));
	}

	@Test
	void basicPostHttpUrlNotAccessibleTest() throws Exception {
		mockMvc.perform(
						post(BASE_API_PATH)
								.accept(MediaType.APPLICATION_JSON_VALUE)
								.param("fileUrl", "http://xhsoifjodjfdsfjkdspejffewfksdfjasbdqdy/user/data.xml.zip")
								.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("status", is(400)));
	}

}
