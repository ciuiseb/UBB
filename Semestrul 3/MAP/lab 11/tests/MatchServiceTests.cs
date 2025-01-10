using lab_11.domain;
using lab_11.service;

namespace lab_11.tests;

public class MatchServiceTests
{
    private readonly MockMatchDatabaseRepository _matchDatabaseRepo;
    private readonly MockActivePlayerDatabaseRepository _activePlayerDatabaseRepo;
    private readonly MockPlayerDatabaseRepository _playerDatabaseRepo;
    private readonly MatchService _service;

    public MatchServiceTests()
    {
        _matchDatabaseRepo = new MockMatchDatabaseRepository();
        _activePlayerDatabaseRepo = new MockActivePlayerDatabaseRepository();
        _playerDatabaseRepo = new MockPlayerDatabaseRepository();
        _service = new MatchService(_matchDatabaseRepo, _activePlayerDatabaseRepo, _playerDatabaseRepo, null);
    }

    private void TestGetMatchScore()
    {
        var testMatch = new Match { Id = 1, Team1Id = 1, Team2Id = 2, Date = DateTime.Now };
        var testMatches = new List<Match> { testMatch };

        var testPlayers = new List<Player>
        {
            new() { Id = 1, TeamId = 1, Name = "Player 1" },
            new() { Id = 2, TeamId = 1, Name = "Player 2" },
            new() { Id = 3, TeamId = 2, Name = "Player 3" }
        };

        var testActivePlayers = new List<ActivePlayer>
        {
            new() { Id = 1, MatchId = 1, PlayerId = 1, PointsScored = 10 },
            new() { Id = 2, MatchId = 1, PlayerId = 2, PointsScored = 15 },
            new() { Id = 3, MatchId = 1, PlayerId = 3, PointsScored = 20 }
        };

        _matchDatabaseRepo.AddTestData(testMatches);
        _playerDatabaseRepo.AddTestData(testPlayers);
        _activePlayerDatabaseRepo.AddTestData(testActivePlayers);

        var (team1Score, team2Score, _, _) = _service.GetMatchScore(1);

        if (team1Score != 25 || team2Score != 20)
            throw new Exception();
    }

    public void RunTests()
    {
        TestGetMatchScore();
    }
}