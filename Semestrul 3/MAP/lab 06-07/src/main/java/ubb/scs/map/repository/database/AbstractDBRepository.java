package ubb.scs.map.repository.database;

import ubb.scs.map.domain.entities.Entity;
import ubb.scs.map.domain.exceptions.RepoException;
import ubb.scs.map.domain.exceptions.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.memory.InMemoryRepository;

import javax.swing.text.html.Option;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected final Validator<E> validator;
    protected final String tableName;

    public AbstractDBRepository(Validator<E> validator, String tableName) {
        this.validator = validator;
        this.tableName = tableName;
    }

    protected abstract String getInsertParameters();
    protected abstract String getUpdateParameters();
    protected abstract int getUpdateParameterCount();

    /**
     * Creates an entity from a DB set of data
     *
     * @param data - the data to be converted
     * @return the entity
     */
    protected abstract E createEntity(ResultSet data);

    /**
     * Updates the data of a db row
     *
     * @param entity - the entity to be converted
     */
    protected abstract void saveEntity(PreparedStatement preparedStatement, E entity);


    @Override
    public Optional<E> save(E entity) throws RepoException {
        try {
            validator.validate(entity);
        } catch (ValidationException e) {
            throw new RepoException(e);
        }

        if(findOne(entity.getId()).isPresent())
            return Optional.of(entity);
        String sql = String.format("INSERT INTO %s VALUES %s", tableName, getInsertParameters());
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            saveEntity(statement, entity);

            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    @Override
    public Optional<E> delete(ID id) throws RepoException{
        var entity = findOne(id);
        if(entity.isEmpty())
            return Optional.empty();

        String sql = String.format("DELETE FROM %s WHERE id = ?", tableName);
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, id);
            statement.executeUpdate();

            return entity;
        }catch(SQLException e){
            throw new RepoException(e);
        }
    }

    @Override
    public Optional<E> update(E entity) throws RepoException{
        String sql = String.format("UPDATE %s SET %s WHERE id = ?", tableName, getUpdateParameters());
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            saveEntity(statement, entity);
            statement.setObject(getUpdateParameterCount() + 1, entity.getId());

            int rowsAffected= statement.executeUpdate();
            return rowsAffected == 0 ? Optional.of(entity) : Optional.empty();
        }catch (SQLException er){
            throw new RepoException(er);
        }
    }

    @Override
    public Optional<E> findOne(ID id) throws RepoException {
        String sql = String.format("SELECT * FROM %s WHERE id = ?", tableName);

        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return Optional.of(createEntity(result));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    @Override
    public Iterable<E> findAll() throws RepoException {
        List<E> entities = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", tableName);

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                entities.add(createEntity(result));
            }
            return entities;
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    public int size() {
        return ((Collection<?>) findAll()).size();
    }

    protected Connection getConnection() throws RepoException {
        try {
            String URL = "jdbc:postgresql://localhost:5432/socialnetwork";
            String USER = "postgres";
            String PASSWORD = "7979";
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RepoException("DB error: " + e.getMessage());
        }
    }
    protected Iterable<E> createList(ResultSet resultSet) {
        try {
            List<E> list = new ArrayList<>();
            while (resultSet.next()) {
                E entity = createEntity(resultSet);
                list.add(entity);
            }
            return list;
        } catch (SQLException e) {
            throw new RepoException("Error creating list from ResultSet: " + e.getMessage());
        }
    }
}