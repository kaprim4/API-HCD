package com.example.HCOData.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {

	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  private String message;

	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  private int code;

	  @JsonInclude(JsonInclude.Include.NON_NULL)
	  private HttpStatus status;

}
