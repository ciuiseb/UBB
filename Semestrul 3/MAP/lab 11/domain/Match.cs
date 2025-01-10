namespace lab_11.domain;

public class Match : Entity<int>
{
    public int Team1Id { get; set; }
    public int Team2Id { get; set; }
    public DateTime Date { get; set; }
}