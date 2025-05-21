using Newtonsoft.Json.Converters;
using System.Text.Json.Serialization;

namespace Triathlon.Network.JsonProtocol;

[JsonConverter(typeof(StringEnumConverter))]
public enum ResponseType
{
    OK,
    ERROR,
    RESULT_UPDATED
}