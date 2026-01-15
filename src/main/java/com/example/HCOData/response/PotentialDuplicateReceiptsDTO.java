package com.example.HCOData.response;


import lombok.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PotentialDuplicateReceiptsDTO {

    private String uid;
    private List<String> flags;

}
