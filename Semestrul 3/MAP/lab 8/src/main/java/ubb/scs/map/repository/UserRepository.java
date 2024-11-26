package ubb.scs.map.repository;

import ubb.scs.map.domain.entities.User;

import java.security.MessageDigest;

public interface UserRepository extends Repository<Long, User> {

    public String encrypt(String input) throws Exception;
}
