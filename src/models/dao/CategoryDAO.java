package models.dao;

import java.util.List;

import models.entities.Category;

public interface CategoryDAO {
    public void insert(Category obj);

    public void update(Category obj);

    public void deleteById(Integer id);

    public List<Category> findAll();

    public Category findById(Integer id);
}
