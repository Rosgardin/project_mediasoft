package com.myexample.mydemo.services;

import com.myexample.mydemo.domain.User;
import com.myexample.mydemo.exceptions.MyAuthException;

public interface IUserService {
    User validateUser(String email, String password) throws MyAuthException;
    User registerUser(String username, String email, String password) throws MyAuthException;
    
}
