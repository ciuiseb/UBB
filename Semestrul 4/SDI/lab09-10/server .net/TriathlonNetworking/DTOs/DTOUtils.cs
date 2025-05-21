using System.Collections.Generic;
using Triathlon.Model;

namespace Triathlon.Network.Dto
{
    public static class DTOUtils
    {
        public static RefereeDTO? FromReferee(Referee referee)
        {
            if (referee == null) return null;
            return new RefereeDTO(
                referee.Id,
                referee.Username,
                referee.Password
            );
        }

        public static Referee? ToReferee(RefereeDTO dto)
        {
            if (dto == null) return null;
            Referee referee = new Referee(
                dto.Username,
                dto.Password,
                "", // email
                "", // firstName
                ""  // lastName
            );
            referee.Id = dto.Id;
            return referee;
        }

        public static ResultDTO FromResult(Result result)
        {
            if (result == null) return null;
            return new ResultDTO(
                result.Id,
                result.Event.Id,
                result.Participant.Id,
                result.Discipline,
                result.Points
            );
        }

        public static List<ResultDTO> FromResultList(List<Result> results)
        {
            List<ResultDTO> dtos = new List<ResultDTO>();
            foreach (Result result in results)
            {
                dtos.Add(FromResult(result));
            }
            return dtos;
        }
    }
}