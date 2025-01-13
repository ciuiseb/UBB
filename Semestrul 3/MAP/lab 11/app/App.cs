namespace lab_11.app;

public class App(Settings settings)
{
    private readonly IController _controller = new Controller(settings.ConnectionString);
    private readonly Logger _logger = new(settings.LogFilePath);


    private static void Menu()
    {
        Console.WriteLine("\nBasketball Management System");
        Console.WriteLine("1. View Team Players");
        Console.WriteLine("2. View Active Players in Match");
        Console.WriteLine("3. View Matches in Date Range");
        Console.WriteLine("4. View Match Score");
        Console.WriteLine("0. Exit");
        Console.Write("Enter your choice: ");
    }

    public void Start()
    {
        _logger.Log("--------------- Application started ---------------");

        while (true)
        {
            Menu();
            var choice = Console.ReadLine();

            try
            {
                switch (choice)
                {
                    case "1":
                        _logger.Log("Viewing team players");
                        ViewTeamPlayers();
                        break;
                    case "2":
                        _logger.Log("Viewing active players in match");
                        ViewActivePlayersInMatch();
                        break;
                    case "3":
                        _logger.Log("Viewing matches in date range");
                        ViewMatchesInDateRange();
                        break;
                    case "4":
                        _logger.Log("Viewing match score");
                        ViewMatchScore();
                        break;
                    case "0":

                        _logger.Log("--------------- Application shutting down ---------------");
                        return;
                    default:
                        _logger.Log($"Invalid choice entered: {choice}");
                        Console.WriteLine("Invalid choice.");
                        break;
                }
            }
            catch (Exception ex)
            {
                _logger.Log($"Error occurred: {ex.Message}");
                Console.WriteLine($"Error: {ex.Message}");
            }

            Console.WriteLine("\nPress Enter to continue... ");
            Console.ReadKey();
            Console.Clear();
        }
    }

    private void ViewTeamPlayers()
    {
        Console.Write("Enter team name: ");
        var teamName = Console.ReadLine();
        if (string.IsNullOrWhiteSpace(teamName))
        {
            _logger.Log("Empty team name entered");
            return;
        }

        var players = _controller.GetTeamPlayers(teamName);
        _logger.Log($"Retrieved {players.Count()} players for team {teamName}");

        Console.WriteLine("\nTeam Players:");
        foreach (var player in players)
        {
            Console.WriteLine($"ID: {player.Id}, Name: {player.Name}");
        }
    }

    private void ViewActivePlayersInMatch()
    {
        Console.Write("Enter team name: ");
        var teamName = Console.ReadLine();
        if (string.IsNullOrWhiteSpace(teamName))
        {
            _logger.Log("Empty team name entered");
            return;
        }

        Console.Write("Enter date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime date))
        {
            _logger.Log("Invalid date format entered");
            return;
        }

        var matchesOnDate = _controller.GetMatchesOnDate(date);
        var match = matchesOnDate.FirstOrDefault(m =>
            _controller.GetTeamName(m.Team1Id) == teamName ||
            _controller.GetTeamName(m.Team2Id) == teamName);

        if (match == null)
        {
            _logger.Log($"No match found for team {teamName} on {date:yyyy-MM-dd}");
            Console.WriteLine($"No match found for team {teamName} on {date:yyyy-MM-dd}");
            return;
        }

        int teamId = _controller.GetTeamName(match.Team1Id) == teamName ? match.Team1Id : match.Team2Id;
        var activePlayers = _controller.GetTeamActivePlayersInMatch(teamId, match.Id);
        _logger.Log($"Found {activePlayers.Count()} active players for team {teamName}");

        Console.WriteLine("\nActive Players:");
        foreach (var player in activePlayers)
        {
            Console.WriteLine($"ID: {player.Id}, Player: {_controller.GetPlayerName(player.PlayerId)}, Points: {player.PointsScored}");
        }
    }

    private void ViewMatchesInDateRange()
    {
        Console.Write("Enter start date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime startDate))
        {
            _logger.Log("Invalid start date format entered");
            return;
        }

        Console.Write("Enter end date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime endDate))
        {
            _logger.Log("Invalid end date format entered");
            return;
        }

        var matches = _controller.GetMatchesInDateRange(startDate, endDate);
        _logger.Log($"Retrieved {matches.Count()} matches between {startDate:yyyy-MM-dd} and {endDate:yyyy-MM-dd}");

        Console.WriteLine("\nMatches in range:");
        foreach (var match in matches)
        {
            Console.WriteLine(
                $"ID: {match.Id}, Teams: {_controller.GetTeamName(match.Team1Id)} vs {_controller.GetTeamName(match.Team2Id)}, Date: {match.Date}"
            );
        }
    }

    private void ViewMatchScore()
    {
        Console.Write("Enter date (yyyy-MM-dd): ");
        if (!DateTime.TryParse(Console.ReadLine(), out DateTime date))
        {
            _logger.Log("Invalid date format entered");
            return;
        }

        var matchesOnDate = _controller.GetMatchesOnDate(date);
        if (matchesOnDate.Count == 0)
        {
            _logger.Log($"No matches found on {date:yyyy-MM-dd}");
            Console.WriteLine($"No matches found on {date:yyyy-MM-dd}");
            return;
        }

        Console.WriteLine("\nMatches on this date:");
        foreach (var match in matchesOnDate)
        {
            var team1Name = _controller.GetTeamName(match.Team1Id);
            var team2Name = _controller.GetTeamName(match.Team2Id);
            Console.WriteLine($"{match.Id}. {team1Name} vs {team2Name}");
        }

        Console.Write("\nEnter match ID to see score: ");
        if (!int.TryParse(Console.ReadLine(), out int matchId))
        {
            _logger.Log("Invalid match ID entered");
            return;
        }

        var (team1Score, team2Score, team1Id, team2Id) = _controller.GetMatchScoreAndTeams(matchId);
        _logger.Log($"Retrieved score for match {matchId}");

        Console.WriteLine(
            $"\nScore: {_controller.GetTeamName(team1Id)}: {team1Score}, {_controller.GetTeamName(team2Id)}: {team2Score}");
    }
}