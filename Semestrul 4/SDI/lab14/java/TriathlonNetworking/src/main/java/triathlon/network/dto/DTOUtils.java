package triathlon.network.dto;

import triathlon.model.*;
import java.util.ArrayList;
import java.util.List;

public class DTOUtils {

    public static RefereeDTO fromReferee(Referee referee) {
        if (referee == null) return null;
        return new RefereeDTO(
                referee.getId(),
                referee.getUsername(),
                referee.getPassword()
        );
    }

    public static Referee toReferee(RefereeDTO dto) {
        if (dto == null) return null;
        Referee referee = new Referee(
                dto.getUsername(),
                dto.getPassword(),
                "", 
                "", 
                ""  
        );
        referee.setId(dto.getId());
        return referee;
    }

    public static ResultDTO fromResult(Result result) {
        if (result == null) return null;
        return new ResultDTO(
                result.getId(),
                result.getEvent().getId(),
                result.getParticipant().getId(),
                result.getDiscipline(),
                result.getPoints()
        );
    }

    public static List<ResultDTO> fromResultList(List<Result> results) {
        List<ResultDTO> dtos = new ArrayList<>();
        for (Result result : results) {
            dtos.add(fromResult(result));
        }
        return dtos;
    }
}