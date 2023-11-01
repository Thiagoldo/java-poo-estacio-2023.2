package models.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.App;
import db.DB;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import models.dao.AssetDAO;
import models.dao.AssetSectorDAO;
import models.dao.DaoFactory;
import models.dao.SectorDAO;
import models.dao.UserDAO;
import models.entities.AssetSector;

public class AssetSectorDaoJdbc implements AssetSectorDAO {

    private Connection conn;
    private DateTimeFormatter fmt;

    public AssetSectorDaoJdbc(Connection connection) {
        this.conn = connection;
        this.fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    private boolean existsAssetSector(AssetSector assetSector) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM AtivoSetor WHERE ativo = ? AND setor = ? AND data_inicio = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setInt(1, assetSector.getAtivo().getId());
            st.setInt(2, assetSector.getSetor().getId());
            st.setString(3, assetSector.getDataInicio().format(fmt));

            rs = st.executeQuery();

            if (rs.next()) {
                Alerts.showAlert("Atenção!", "Duplicidade!",
                        assetSector.getAtivo() + " já cadastrado no setor " + assetSector.getSetor() + " nada data " + assetSector.getDataInicio().format(fmt) + ".", AlertType.WARNING);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
            return false;
        } finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void insert(AssetSector obj) {

        PreparedStatement st = null;
        String sql_statement = """
                INSERT INTO AtivoSetor (
                    ativo,
                    setor,
                    data_inicio,
                    cadastrado_por,
                    cadastrado_em
                    ) VALUES (
                        ?, ?, ?, ?, ?
                    );
                    """;

        if (!existsAssetSector(obj)) {
            try {
                st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

                st.setInt(1, obj.getAtivo().getId());
                st.setInt(2, obj.getSetor().getId());
                st.setString(3, obj.getDataInicio().format(fmt));

                st.setInt(4, App.getAUTHENTICATED_USER().getId());
                st.setTimestamp(5, Timestamp.from(Instant.now()));

                st.executeUpdate();

            } catch (

            Exception e) {
                Alerts.showAlert(
                        "SQL Exception",
                        "Error in DataBase",
                        e.getMessage(),
                        AlertType.ERROR);
            } finally {
                DB.closeStatement(st);
            }
        }
    }

    @Override
    public void update(AssetSector obj) {
        PreparedStatement st = null;
        String sql_statement = """
                UPDATE AtivoSetor SET
                data_fim = ?,
                alterado_por = ?,
                alterado_em = ?,
                observacao = ?
                WHERE ativo = ? and setor = ? AND data_inicio = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getDataFim().format(fmt));

            st.setInt(2, obj.getAlteradoPor().getId());
            st.setTimestamp(3, Timestamp.from(Instant.now()));
            st.setString(4, obj.getObservacao());

            st.setInt(5, obj.getAtivo().getId());
            st.setInt(6, obj.getSetor().getId());
            st.setString(7, obj.getDataInicio().format(fmt));

            st.executeUpdate();

        } catch (Exception e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
        } finally {
            DB.closeStatement(st);
        }
    }

    // @Override
    // public void deleteById(Integer id) {
    // PreparedStatement st = null;
    // String sql_statement = """
    // DELETE FROM Setores WHERE id = ?;
    // """;

    // try {
    // st = conn.prepareStatement(sql_statement);

    // st.setInt(1, id);

    // int rowsAffected = st.executeUpdate();

    // if (rowsAffected > 0) {
    // Alerts.showAlert("Sucesso!", "Cadastro deletado com sucesso!",
    // "Setor deletado.", AlertType.INFORMATION);
    // } else {
    // throw new SQLException("Unexpected erros: None rows affected!");
    // }
    // } catch (Exception e) {
    // Alerts.showAlert(
    // "SQL Exception",
    // "Error in DataBase",
    // e.getMessage(),
    // AlertType.ERROR);
    // } finally {
    // DB.closeStatement(st);
    // }
    // }

    @Override
    public List<AssetSector> findAll() {
        List<AssetSector> ativosSetores = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM AtivoSetor;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            rs = st.executeQuery();

            while (rs.next()) {
                AssetSector aux = new AssetSector();
                UserDAO user = DaoFactory.createUserDAO();
                AssetDAO asset = DaoFactory.createAssetDAO();
                SectorDAO sector = DaoFactory.createSectorDAO();

                aux.setAtivo(asset.findById(rs.getInt("ativo")));
                aux.setSetor(sector.findById(rs.getInt("setor")));
                aux.setObservacao(rs.getString("observacao"));

                if (rs.getString("data_inicio") != null) {
                    aux.setDataInicio(LocalDate.parse(rs.getString("data_inicio"), fmt));
                }

                if (rs.getString("data_fim") != null) {
                    aux.setDataFim(LocalDate.parse(rs.getString("data_fim"), fmt));
                }

                aux.setCadastradoPor(user.findById(rs.getInt("cadastrado_por")));
                aux.setAlteradoPor(user.findById(rs.getInt("alterado_por")));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    aux.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    aux.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }

                ativosSetores.add(aux);
            }

            return ativosSetores;
        } catch (Exception e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
        } finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
        }

        return null;
    }

    @Override
    public AssetSector findOne(Integer asset, Integer sector, LocalDate dataInicio) {

        UserDAO userDAO = DaoFactory.createUserDAO();

        AssetDAO assetDAO = DaoFactory.createAssetDAO();
        SectorDAO sectorDAO = DaoFactory.createSectorDAO();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM AtivoSetor WHERE ativo = ? AND setor = ? AND data_inicio = ?;
                """;

        try {

            st = conn.prepareStatement(select_statement);

            st.setInt(1, asset);
            st.setInt(2, sector);
            st.setString(3, dataInicio.format(fmt));

            rs = st.executeQuery();

            if (rs.next()) {
                AssetSector aux = new AssetSector();

                aux.setAtivo(assetDAO.findById(rs.getInt("ativo")));
                aux.setSetor(sectorDAO.findById(rs.getInt("setor")));

                if (rs.getString("data_inicio") != null) {
                    aux.setDataInicio(LocalDate.parse(rs.getString("data_inicio"), fmt));
                }
                if (rs.getString("data_fim") != null) {
                    aux.setDataFim(LocalDate.parse(rs.getString("data_fim"), fmt));
                }

                aux.setObservacao(rs.getString("observacao"));

                aux.setCadastradoPor(userDAO.findById(rs.getInt("cadastrado_por")));
                aux.setAlteradoPor(userDAO.findById(rs.getInt("alterado_por")));

                if (rs.getTimestamp("cadastrado_em") != null) {
                    aux.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    aux.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }
                return aux;
            }
        } catch (Exception e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
        } finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
        }
        return null;
    }

}