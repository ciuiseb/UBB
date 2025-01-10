using lab_11.domain;
using lab_11.repo;

namespace lab_11.service;

/// <summary>
/// Service class for handling team-related operations
/// </summary>
/// <param name="teamRepository">The repository used for team data access</param>
public class TeamService(IRepository<Team, int> teamRepository)
{
    /// <summary>
    /// Retrieves the name of a team by its ID
    /// </summary>
    /// <param name="teamId">The ID of the team to find</param>
    /// <returns>The team's name if found, null otherwise</returns>
    public string? GetTeamName(int teamId)
    {
        var team = teamRepository.FindOne(teamId);
        return team?.Name;
    }
}