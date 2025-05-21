using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using Triathlon.Model;

namespace Triathlon.Network.JsonProtocol
{
    [Serializable]
    public class Response
    {
        private ResponseType type;
        private string errorMessage;
        private RefereeDiscipline refereeDiscipline;
        private List<Result> results;
        private int totalPoints;

        public Response()
        {
        }

        public ResponseType Type
        {
            get { return type; }
            set { type = value; }
        }

        public RefereeDiscipline RefereeDiscipline
        {
            get { return refereeDiscipline; }
            set { refereeDiscipline = value; }
        }

        [JsonIgnore]
        public Event Event
        {
            get { return refereeDiscipline?.Event; }
        }

        [JsonIgnore]
        public Discipline Discipline
        {
            get { return refereeDiscipline.Discipline; }
        }

        public string ErrorMessage
        {
            get { return errorMessage; }
            set { errorMessage = value; }
        }

        [JsonIgnore]
        public Referee Referee
        {
            get { return refereeDiscipline?.Referee; }
        }

        public List<Result> Results
        {
            get { return results; }
            set { results = value; }
        }

        public int TotalPoints
        {
            get { return totalPoints; }
            set { totalPoints = value; }
        }

        public override string ToString()
        {
            return "Response{" +
                   "type=" + type +
                   ", errorMessage='" + errorMessage + '\'' +
                   ", referee=" + (refereeDiscipline != null ? Referee?.ToString() : "null") +
                   ", event=" + (refereeDiscipline != null ? Event?.ToString() : "null") +
                   ", discipline=" + (refereeDiscipline != null ? Discipline.ToString() : "null") +
                   ", results=" + results +
                   '}';
        }
    }
}