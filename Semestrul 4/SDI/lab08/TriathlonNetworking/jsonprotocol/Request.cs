using System;

namespace Triathlon.Network.JsonProtocol;

[Serializable]
public class Request
{
    private RequestType type;
    private object data;

    public Request()
    {
    }

    public RequestType Type
    {
        get { return type; }
        set { type = value; }
    }

    public object Data
    {
        get { return data; }
        set { data = value; }
    }

    public override string ToString()
    {
        return "Request{" +
               "type=" + type +
               ", data=" + data +
               '}';
    }
}