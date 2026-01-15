package com.example.HCOData.response;


import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MatchedDTO {

    private List<ItemDTO> items;
    private boolean isMatched;
    private List<String> itemsMatched;
}
