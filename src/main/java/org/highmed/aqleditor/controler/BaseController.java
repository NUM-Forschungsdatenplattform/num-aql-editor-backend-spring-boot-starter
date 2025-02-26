

package org.highmed.aqleditor.controler;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.ehrbase.openehr.sdk.aql.parser.AqlParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public abstract class BaseController {

  @ExceptionHandler(AqlParseException.class)
  public ResponseEntity<Map<String, String>> aqlParseErrorHandler(RuntimeException e) {
    log.error(e.getMessage(), e);
    return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> restErrorHandler(RuntimeException e) {
    log.error(e.getMessage(), e);
    return createErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  protected ResponseEntity<Map<String, String>> createErrorResponse(
      String message, HttpStatus status) {
    Map<String, String> error = new HashMap<>();
    error.put("error", message);
    error.put("status", status.getReasonPhrase());
    return new ResponseEntity<>(error, status);
  }
}
