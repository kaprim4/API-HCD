package com.example.HCOData.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HCDResponseDTO {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String uid;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
 
}
