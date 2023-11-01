package models.dao;

import java.util.List;

import models.entities.Asset;

public interface AssetDAO {
    public void insert(Asset obj);

    public void update(Asset obj);

    public void deleteById(Integer id);

    public List<Asset> findAll();

    public Asset findById(Integer id);
}
