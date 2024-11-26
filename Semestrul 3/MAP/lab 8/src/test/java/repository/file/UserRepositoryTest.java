//package repository.file;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//
//import ubb.scs.map.domain.entities.User;
//import ubb.scs.map.domain.exceptions.RepoException;
//import ubb.scs.map.domain.validators.UserValidator;
//import ubb.scs.map.domain.exceptions.ValidationException;
//import ubb.scs.map.repository.file.UserFileRepository;
//
//import java.util.Collection;
//
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class UserRepositoryTest {
//
//    private UserFileRepository repo;
//
//    @BeforeEach
//    public void SetUp() {
//        var validator = new UserValidator();
//        repo = new UserFileRepository(validator, "src/test/resources/test_user_file.txt");
//    }
//
//    // findOne() tests
//    @Test
//    public void FindOneTestFound() {
//        var found = repo.findOne(1L);
//        assert (found.isPresent());
//    }
//
//    @Test
//    public void FindOneTestNotFound() {
//        var found = repo.findOne(0L);
//        assert (found.isEmpty());
//    }
//
//    @Test
//    public void FindOneTestNull() {
//        assertThrows(RepoException.class, () -> repo.findOne(null));
//    }
//
//    // findAll() tests
//    @Test
//    public void FindAllTestFound() {
//        var found = repo.findAll();
//        assert (((Collection<?>) found).size() == 2);
//    }
//    // save(E) tests
//    @Test
//    public void SaveTestSuccesfull(){
//        User e = new User("vlad", "mihai");
//        repo.save(e);
//        assert(repo.size() == 3);
//    }
//    @Test
//    public void SaveTestNotValid(){
//        User e = new User("vlad", "");
//        try{
//            repo.save(e);
//        }catch(ValidationException ignored){
//            assert(repo.size() == 2);
//        }
//
//        e = new User("", "asd");
//        try{
//            repo.save(e);
//        }catch(ValidationException ignored){
//            assert(repo.size() == 2);
//        }
//
//    }
//    @Test
//    public void SaveTestNull(){
//        User e = null;
//        assertThrows(RepoException.class, () ->repo.save(e));
//        assert(repo.size() == 2);
//    }
//    // delete(ID) tests
//    @Test
//    public void DeleteTestFound() {
//        var deleted = repo.delete(1L);
//        assert (deleted
//                .get()
//                .getFirstName()
//                .equals("IONUT"));
//        assert (repo.size() == 1);
//    }
//
//    @Test
//    public void DeleteTestNotFound() {
//        assertThrows(RepoException.class, () -> repo.delete(0L));
//        assert (repo.size() == 2);
//    }
//
//    @Test
//    public void DeleteTestNull() {
//        assertThrows(RepoException.class, () -> repo.delete(null));
//    }
//
//    // update(E) tests
//    @Test
//    public void UpdateTestFound() {
//
//    }
//
//    @Test
//    public void UpdateTestNotFound() {
//
//    }
//
//    @Test
//    public void UpdateTestNull() {
//
//    }
//
//    @Test
//    public void UpdateTestNotValid() {
//
//    }
//
//    @AfterEach
//    public void TearDown() {
//        try {
//            Files.copy(Path.of("src/test/resources/test_user_file_backup.txt"),
//                    Path.of("src/test/resources/test_user_file.txt"),
//                    StandardCopyOption.REPLACE_EXISTING);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
