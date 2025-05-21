namespace cs.Domain;
public abstract class Entity<ID>
{
    protected ID id;

    public Entity(ID id)
    {
        this.id = id;
    }

    public Entity()
    {
    }

    public ID GetId()
    {
        return id;
    }

    public void SetId(ID id)
    {
        this.id = id;
    }
}