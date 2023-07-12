package com.myexample.mydemo.repositories;

import com.myexample.mydemo.domain.User;
import com.myexample.mydemo.exceptions.MyAuthException;

public interface UserRepository {
    Integer create(String username, String email, String password) throws MyAuthException;
    User findByEmailAndPassword(String email, String password) throws MyAuthException;
    Integer getCountByEmail(String email);
    User findById(Integer userId);
}
