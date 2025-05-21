namespace cs.Domain;
public abstract class IEntity<ID>
{
    protected ID id;

    public IEntity(ID id)
    {
        this.id = id;
    }

    public IEntity()
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