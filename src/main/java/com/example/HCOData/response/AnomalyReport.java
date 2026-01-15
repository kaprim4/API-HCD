package com.example.HCOData.response;

import com.example.HCOData.enums.ExistFileFlagsEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnomalyReport {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> flags;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PotentialDuplicateReceiptsDTO> potentialDuplicateReceipts;



}
