namespace lab_11.domain;

public abstract class Entity<TId>
{
    public TId Id { get; set; }
}