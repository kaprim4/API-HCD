package com.example.HCOData.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CheckPotentialDuplicateReceiptsDTO {
    private Map<String, Set<String>> potentialDuplicateReceipts;
    private boolean duplicate;
}
