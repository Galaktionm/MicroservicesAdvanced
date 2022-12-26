package util.http;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class HttpErrorInfo {
	
  private ZonedDateTime timestamp;
  private String path;
  private HttpStatus httpStatus;
  private String message;

  public HttpErrorInfo() {}

  public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
    this.timestamp = ZonedDateTime.now();
    this.httpStatus = httpStatus;
    this.path = path;
    this.message = message;
  }
}