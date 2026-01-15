package com.example.HCOData.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ImageResponseDTO {
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String uid;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdAt;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedAt;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String dominantLanguage;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String processReport;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private AnomalyReport anomalyReport;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String imageUrl;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String endpointUid;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String companyUid;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String campaignUid;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private List<BareCodesDTO> barcodes;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private DataDTO data;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private ProcessedRules processedRules;
}
