using Triathlon.Model;

namespace Triathlon.Services;

public interface ITriathlonObserver
{
    void ResultsUpdated(Result result);
}