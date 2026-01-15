package com.example.HCOData.request;

import com.example.HCOData.model.Image;
import com.example.HCOData.model.Operation;
import com.example.HCOData.model.ParametresPassageToModeration;
import com.example.HCOData.model.ResponseAI;
import com.example.HCOData.response.ImageResponseDTO;
import com.example.HCOData.response.NewAIResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Data
@Builder
public class ModerationContextDTO {

    private final Operation operation;
    private final String datePA;
    private final Set<String> moderationFlagsList;
    private final Image imageToSave;
    private final NewAIResponseDTO newAIResponseDTO;
    private final ResponseApiImageDTO responseApiImageDTO;
    private final ResponseAI responseAI;
    private final ParametresPassageToModeration passageToModeration;
    private final ImageResponseDTO imageResponseDTO;
}
