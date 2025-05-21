package mpp.repository.abstracts;

import mpp.domain.Entity;
import mpp.repository.interfaces.Repository;
import mpp.utils.DatabaseConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    protected final String tableName;
    protected final String primaryKey;
    protected final Connection connection;


    public AbstractDBRepository(String tableName, String primaryKey) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        try{
            connection = getConnection();
        } catch (Exception e) {
//            log.error(e);
            throw new RuntimeException();
        }
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
    protected abstract E createEntity(ResultSet data) throws SQLException;

    /**
     * Updates the data of a db row
     *
     * @param entity - the entity to be converted
     */
    protected abstract void saveEntity(PreparedStatement preparedStatement, E entity) throws SQLException;


    @Override
    public Optional<E> save(E entity) {
        if (findOne(entity.getId()).isPresent())
            return Optional.of(entity);
        String sql = String.format("INSERT INTO %s VALUES %s", tableName, getInsertParameters());
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            saveEntity(statement, entity);

            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        var entity = findOne(id);
        if (entity.isEmpty())
            return Optional.empty();

        String sql = String.format("DELETE FROM %s WHERE " + primaryKey +" = ?", tableName);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, id);
            statement.executeUpdate();

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> update(E entity) {
        String sql = String.format("UPDATE %s SET %s WHERE %s = ?",
                tableName,
                getUpdateParameters(),
                primaryKey);
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            saveEntity(statement, entity);
            statement.setObject(getUpdateParameterCount() + 1, entity.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException er) {
            throw new RuntimeException(er);
        }
    }


    @Override
    public Optional<E> findOne(ID id) {
        String sql = String.format("SELECT * FROM %s WHERE " + primaryKey + " = ?", tableName);

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, id);

            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                return Optional.of(createEntity(result));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<E> findAll() {
        List<E> entities = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", tableName);

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                entities.add(createEntity(result));
            }
            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return ((Collection<?>) findAll()).size();
    }

    protected Connection getConnection() throws SQLException, IOException {
        return DatabaseConfig.getConnection();
    }

    protected Iterable<E> createList(ResultSet resultSet) throws SQLException {
        try {
            List<E> list = new ArrayList<>();
            while (resultSet.next()) {
                E entity = createEntity(resultSet);
                list.add(entity);
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException("Error creating list from ResultSet: " + e.getMessage());
        }
    }
}
