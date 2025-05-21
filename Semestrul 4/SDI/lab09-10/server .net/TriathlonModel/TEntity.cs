namespace Triathlon.Model
{
    public abstract class TEntity<ID>
    {
        protected ID id;

        public TEntity(ID id)
        {
            this.id = id;
        }

        public TEntity()
        {
        }

        public ID Id
        {
            get { return id; }
            set { id = value; }
        }
    }
}