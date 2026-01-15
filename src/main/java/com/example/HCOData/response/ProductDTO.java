package com.example.HCOData.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String category;

    private String categoryUid;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String brand;

    private String brandUid;

    private String brandLine;

    private String gtin;

}

