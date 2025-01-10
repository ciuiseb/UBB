package ubb.scs.map.repository;

import ubb.scs.map.domain.entities.User;

import java.security.MessageDigest;
import java.util.List;

public interface UserRepository extends Repository<Long, User> {

    String encrypt(String input) throws Exception;

}
