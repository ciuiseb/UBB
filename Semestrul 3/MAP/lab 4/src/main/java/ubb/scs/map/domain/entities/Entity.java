package ubb.scs.map.domain.entities;

public class Entity<ID>  {
    private ID id;
    /**
     * The id of the entity
     * @return the id of the entity
     */
    public ID getId() {
        return id;
    }
    /**
     * Sets the id of the entity
     * @param id the new id of the entity
     */
    public void setId(ID id) {
        this.id = id;
    }
}