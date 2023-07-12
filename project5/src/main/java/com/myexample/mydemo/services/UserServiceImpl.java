package com.myexample.mydemo.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myexample.mydemo.domain.User;
import com.myexample.mydemo.exceptions.MyAuthException;
import com.myexample.mydemo.repositories.UserRepositoryImpl;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepositoryImpl userRepositoryImpl;

    @Override
    public User validateUser(String email, String password) throws MyAuthException {
        if(email != null) email = email.toLowerCase();
        return userRepositoryImpl.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String username, String email, String password) throws MyAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if(email !=null) email = email.toLowerCase();
        if(!pattern.matcher(email).matches()) throw new MyAuthException("Invalid email format");
        Integer count = userRepositoryImpl.getCountByEmail(email);
        if(count > 0) throw new MyAuthException("Email already in use");
        Integer userId = userRepositoryImpl.create(username, email, password);

        return userRepositoryImpl.findById(userId);
    }

}
