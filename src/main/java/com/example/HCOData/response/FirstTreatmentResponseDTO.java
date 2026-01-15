package com.example.HCOData.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstTreatmentResponseDTO {

    private HCDResponseDTO hcdResponseDTO;

    private String responseAi;

    private File file;

	@Override
	public String toString() {
		return "FirstTreatmentResponse [hcdResponse=" + hcdResponseDTO + ", responseAi=" + responseAi + ", file=" + file+ "]";
	}
}
