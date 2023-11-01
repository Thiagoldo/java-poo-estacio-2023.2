package models.dao;

import java.util.List;

import models.entities.User;

public interface UserDAO {
    public void insert(User obj);

    public void update(User obj);

    public void deleteById(Integer id);

    public List<User> findAll();

    public User findById(Integer id);

    public User findByUser(String searchedUser);

}
