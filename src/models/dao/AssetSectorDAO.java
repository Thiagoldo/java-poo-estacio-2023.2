package models.dao;

import java.time.LocalDate;
import java.util.List;

import models.entities.AssetSector;

public interface AssetSectorDAO {

    public void insert(AssetSector obj);

    public void update(AssetSector obj);

    // public void deleteById(Integer id);

    public List<AssetSector> findAll();

    public AssetSector findOne(Integer asset, Integer sector, LocalDate dataInicio);
}
