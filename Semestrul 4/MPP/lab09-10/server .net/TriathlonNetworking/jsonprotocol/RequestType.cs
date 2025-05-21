using Newtonsoft.Json.Converters;
using System.Text.Json.Serialization;

namespace Triathlon.Network.JsonProtocol;

[JsonConverter(typeof(StringEnumConverter))]
public enum RequestType
{
    LOGIN,
    LOGOUT,
    GET_RESULTS_BY_EVENT,
    UPDATE_RESULT,
    GET_TOTAL_POINTS_FOR_PARTICIPANT
}