package cz.hhvitek.xtractql.app;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@JsonPropertyOrder({"timestamp", "status", "message", "more"})
@JsonInclude(JsonInclude.Include.NON_NULL) // ignore null fields in output
public final class ApiResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);
	private final HttpStatus status;
	@JsonProperty
	private String message;
	@JsonProperty
	private String more;

	private ApiResponse(HttpStatus status) {
		this.status = status;
	}

	@JsonProperty("status")
	public int getStatusAsCode() {
		return status.value();
	}

	public static ApiResponse ok() {
		return new ApiResponse(HttpStatus.OK);
	}

	public static ApiResponse badRequest() {
		return new ApiResponse(HttpStatus.BAD_REQUEST);
	}

	public static ApiResponse status(HttpStatus status) {
		return new ApiResponse(status);
	}

	public ApiResponse message(String message) {
		this.message = message;
		return this;
	}

	public ApiResponse more(String more) {
		this.more = more;
		return this;
	}
}
