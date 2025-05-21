using System.Collections.Generic;
using Triathlon.Model;

namespace Triathlon.Network.JsonProtocol;

public static class JsonProtocolUtils
{
    public static Response CreateOkResponse()
    {
        Response resp = new Response();
        resp.Type = ResponseType.OK;
        return resp;
    }

    public static Response CreateErrorResponse(string errorMessage)
    {
        Response resp = new Response();
        resp.Type = ResponseType.ERROR;
        resp.ErrorMessage = errorMessage;
        return resp;
    }

    public static Response CreateResultUpdatedResponse(Result result)
    {
        Response resp = new Response();
        resp.Type = ResponseType.RESULT_UPDATED;
        resp.Results = new List<Result> { result };
        return resp;
    }

    public static Response CreateLoginResponse(RefereeDiscipline assignment)
    {
        Response resp = new Response();
        resp.Type = ResponseType.OK;
        resp.RefereeDiscipline = assignment;
        return resp;
    }

    public static Response CreateGetResultsByEventResponse(List<Result> results)
    {
        Response resp = new Response();
        resp.Type = ResponseType.OK;
        resp.Results = results;
        return resp;
    }

    public static Request CreateLoginRequest(Referee referee)
    {
        Request req = new Request();
        req.Type = RequestType.LOGIN;
        req.Data = referee;
        return req;
    }

    public static Request CreateLogoutRequest(Referee referee)
    {
        Request req = new Request();
        req.Type = RequestType.LOGOUT;
        req.Data = referee;
        return req;
    }

    public static Request CreateGetResultsByEventRequest(long eventId)
    {
        Request req = new Request();
        req.Type = RequestType.GET_RESULTS_BY_EVENT;
        req.Data = eventId;
        return req;
    }

    public static Request CreateUpdateResultRequest(Result result)
    {
        Request req = new Request();
        req.Type = RequestType.UPDATE_RESULT;
        req.Data = result;
        return req;
    }

    public static Request CreateGetTotalPointsForParticipantRequest(long participantId, long eventId)
    {
        Request req = new Request();
        req.Type = RequestType.GET_TOTAL_POINTS_FOR_PARTICIPANT;
        req.Data = new object[] { participantId, eventId };
        return req;
    }
}