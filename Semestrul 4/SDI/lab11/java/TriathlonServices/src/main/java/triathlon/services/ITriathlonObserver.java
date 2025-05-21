package triathlon.services;

import triathlon.model.Result;

public interface ITriathlonObserver {
    void resultsUpdated(Result result) throws TriathlonException;
}