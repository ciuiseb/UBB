package triathlon.network.jsonprotocol;

import triathlon.model.*;

import java.util.List;

public class JsonProtocolUtils {
    public static Response createOkResponse() {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        return resp;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response resp = new Response();
        resp.setType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Response createResultUpdatedResponse(Result result) {
        Response resp = new Response();
        resp.setType(ResponseType.RESULT_UPDATED);
        resp.setResults(List.of(result));
        return resp;
    }

    public static Response createLoginResponse(RefereeDiscipline asssignement) {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        resp.setRefereeDiscipline(asssignement);
        return resp;
    }

    public static Response createGetResultsByEventResponse(List<Result> results) {
        Response resp = new Response();
        resp.setType(ResponseType.OK);
        resp.setResults(results);
        return resp;
    }

    public static Request createLoginRequest(Referee referee) {
        Request req = new Request();
        req.setType(RequestType.LOGIN);
        req.setData(referee);
        return req;
    }

    public static Request createLogoutRequest(Referee referee) {
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        req.setData(referee);
        return req;
    }

    public static Request createGetResultsByEventRequest(Long eventId) {
        Request req = new Request();
        req.setType(RequestType.GET_RESULTS_BY_EVENT);
        req.setData(eventId);
        return req;
    }

    public static Request createUpdateResultRequest(Result result) {
        Request req = new Request();
        req.setType(RequestType.UPDATE_RESULT);
        req.setData(result);
        return req;
    }
    public static Request createGetTotalPointsForParticipantRequest(Long participantId, Long eventId) {
        Request req = new Request();
        req.setType(RequestType.GET_TOTAL_POINTS_FOR_PARTICIPANT);
        req.setData(new Object[]{participantId, eventId});
        return req;
    }

}