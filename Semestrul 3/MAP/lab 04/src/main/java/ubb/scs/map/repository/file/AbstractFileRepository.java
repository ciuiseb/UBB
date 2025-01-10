package ubb.scs.map.repository.file;

import ubb.scs.map.domain.entities.Entity;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    private String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename=fileName;
        readFromFile();
    }

    /**
     * Creates an entity from a string
     * @param line - the string to be converted
     * @return the entity
     */
    public abstract E createEntity(String line);
    /**
     * Creates a string from an entity
     * @param entity - the entity to be converted
     * @return the string
     */
    public abstract String saveEntity(E entity);

    @Override
    public Optional<E> save(E entity) {
        return super.save(entity);
    }
    private void readFromFile(){
        try (var br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                E entity = createEntity(line);
                save(entity);
            }
        } catch (IOException e) {
            throw new RepoException(e);
        }
    }
    /**
     * Writes the entities to the file
     * @throws RepoException if the file cannot be written
     */
    public void writeToFile() {

        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepoException(e);
        }

    }

    @Override
    public Optional<E> delete(ID id) {
        var e = super.delete(id);
        if(e.isEmpty()){
            throw new RepoException("Entity does not exist");
        }
        writeToFile();
        return e;
    }

    @Override
    public Optional<E> update(E entity) {
        var e = super.update(entity);
        if(e.isEmpty()){
            writeToFile();
        }
        return e;
    }
    public int size(){
        return ((Collection<?>) findAll()).size();
    }
}
