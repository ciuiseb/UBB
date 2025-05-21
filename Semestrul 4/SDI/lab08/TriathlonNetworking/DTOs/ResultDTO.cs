using Triathlon.Model;
using System;

namespace Triathlon.Network.Dto
{
    [Serializable]
    public class ResultDTO
    {
        private long id;
        private long eventId;
        private long participantId;
        private Discipline discipline;
        private int points;

        public ResultDTO() { }

        public ResultDTO(long id, long eventId, long participantId, Discipline discipline, int points)
        {
            this.id = id;
            this.eventId = eventId;
            this.participantId = participantId;
            this.discipline = discipline;
            this.points = points;
        }

        public long Id { get { return id; } set { id = value; } }
        public long EventId { get { return eventId; } set { eventId = value; } }
        public long ParticipantId { get { return participantId; } set { participantId = value; } }
        public Discipline Discipline { get { return discipline; } set { discipline = value; } }
        public int Points { get { return points; } set { points = value; } }
    }
}