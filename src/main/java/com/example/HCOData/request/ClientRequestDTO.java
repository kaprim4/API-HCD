package com.example.HCOData.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDTO {

    private String imageName;
    private String imageType;
    private String idOperation;
}
