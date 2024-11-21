package repository.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import ubb.scs.map.domain.entities.User;
import ubb.scs.map.domain.validators.UserValidator;
import ubb.scs.map.domain.exceptions.ValidationException;
import ubb.scs.map.repository.file.UserRepository;

import java.util.Collection;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UserRepositoryTest {

    private UserRepository repo;

    @BeforeEach
    public void SetUp() {
        var validator = new UserValidator();
        repo = new UserRepository(validator, "src/test/resources/test_user_file.txt");
    }

    // findOne() tests
    @Test
    public void FindOneTestFound() {
        var found = repo.findOne(1L);
        assert (found != null);
    }

    @Test
    public void FindOneTestNotFound() {
        var found = repo.findOne(0L);
        assert (found == null);
    }

    @Test
    public void FindOneTestNull() {
        try {
            var found = repo.findOne(null);
            assert (false);
        } catch (IllegalArgumentException ignored) {
        }

    }

    // findAll() tests
    @Test
    public void FindAllTestFound() {
        var found = repo.findAll();
        assert (((Collection<?>) found).size() == 2);
    }
    // save(E) tests
    @Test
    public void SaveTestSuccesfull(){
        User e = new User("vlad", "mihai");
        repo.save(e);
        assert(repo.size() == 3);
    }
    @Test
    public void SaveTestNotValid(){
        User e = new User("vlad", "");
        try{
            repo.save(e);
        }catch(ValidationException ignored){
            assert(repo.size() == 2);
        }

        e = new User("", "asd");
        try{
            repo.save(e);
        }catch(ValidationException ignored){
            assert(repo.size() == 2);
        }

    }
    @Test
    public void SaveTestNull(){
        User e = null;
        try{
            repo.save(e);
        }catch(IllegalArgumentException ignored){
            assert(repo.size() == 2);
        }
    }
    // delete(ID) tests
    @Test
    public void DeleteTestFound() {
        var deleted = repo.delete(1L);
        assert (deleted.getFirstName().equals("IONUT"));
        assert (repo.size() == 1);
    }

    @Test
    public void DeleteTestNotFound() {
        var deleted = repo.delete(0L);
        assert (deleted == null);
        assert (repo.size() == 1);
    }

    @Test
    public void DeleteTestNull() {
        try{
            repo.delete(null);
            assert (false);
        }catch (IllegalArgumentException ignored){}
    }

    // update(E) tests
    @Test
    public void UpdateTestFound() {

    }

    @Test
    public void UpdateTestNotFound() {

    }

    @Test
    public void UpdateTestNull() {

    }

    @Test
    public void UpdateTestNotValid() {

    }

    @AfterEach
    public void TearDown() {
        try {
            Files.copy(Path.of("src/test/resources/test_user_file_backup.txt"),
                    Path.of("src/test/resources/test_user_file.txt"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
