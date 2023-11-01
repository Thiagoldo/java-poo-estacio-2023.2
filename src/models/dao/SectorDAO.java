package models.dao;

import java.util.List;

import models.entities.Sector;

public interface SectorDAO {

    public void insert(Sector obj);

    public void update(Sector obj);

    public void deleteById(Integer id);

    public List<Sector> findAll();

    public Sector findById(Integer id);
}
