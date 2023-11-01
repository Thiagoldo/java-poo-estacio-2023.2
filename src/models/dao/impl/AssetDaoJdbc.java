package models.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import application.App;
import db.DB;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import models.dao.AssetDAO;
import models.dao.CategoryDAO;
import models.dao.DaoFactory;
import models.dao.UserDAO;
import models.entities.Asset;
import models.enums.Status;

public class AssetDaoJdbc implements AssetDAO {

    private Connection conn = null;

    public AssetDaoJdbc(Connection connection) {
        this.conn = connection;
    }

    @Override
    public void insert(Asset obj) {
        PreparedStatement st = null;
        String sql_statement = """
                INSERT INTO Ativos (
                    descricao,
                    categoria,
                    numero_serie,
                    nota_fiscal,
                    modelo,
                    imagem,
                    valor,
                    status,
                    observacao,
                    cadastrado_por,
                    cadastrado_em
                ) VALUES (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                );
                """;

        try {
            st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getDescricao());
            st.setInt(2, obj.getCategoria().getId());
            st.setString(3, obj.getNumeroSerie());
            st.setString(4, obj.getNotaFiscal());
            st.setString(5, obj.getModelo());
            st.setString(6, obj.getImagem());
            st.setDouble(7, obj.getValor());
            st.setString(8, obj.getStatus().name());
            st.setString(9, obj.getObservacao());

            st.setInt(10, App.getAUTHENTICATED_USER().getId());

            LocalDateTime now = LocalDateTime.now();

            st.setTimestamp(11, Timestamp.valueOf(now));

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Statement stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                    obj.setCadastradoEm(now);
                }
                DB.closeResultset(rs);
            } else {
                throw new SQLException("Unexpected erros: None rows affected!");
            }

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

    @Override
    public void update(Asset obj) {
        PreparedStatement st = null;
        String sql_statement = """
                UPDATE Ativos SET
                    descricao = ?,
                    categoria = ?,
                    numero_serie = ?,
                    nota_fiscal = ?,
                    modelo = ?,
                    imagem = ?,
                    valor = ?,
                    status = ?,
                    observacao = ?,
                    alterado_por = ?,
                    alterado_em = ?
                WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getDescricao());
            st.setInt(2, obj.getCategoria().getId());
            st.setString(3, obj.getNumeroSerie());
            st.setString(4, obj.getNotaFiscal());
            st.setString(5, obj.getModelo());
            st.setString(6, obj.getImagem());
            st.setDouble(7, obj.getValor());
            st.setString(8, obj.getStatus().name());
            st.setString(9, obj.getObservacao());

            st.setInt(10, App.getAUTHENTICATED_USER().getId());

            LocalDateTime now = LocalDateTime.now();

            st.setTimestamp(11, Timestamp.valueOf(now));
            st.setInt(12, obj.getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro alterado com sucesso!",
                        "Ativo " + obj.getDescricao() + " alterado.", AlertType.INFORMATION);
            } else {
                throw new SQLException("Unexpected erros: None rows affected!");
            }

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

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        String sql_statement = """
                DELETE FROM Ativos WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement);

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro deletado com sucesso!",
                        "Ativo deletado.", AlertType.INFORMATION);
            } else {
                throw new SQLException("Unexpected erros: None rows affected!");
            }
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

    @Override
    public List<Asset> findAll() {

        List<Asset> assets = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = "SELECT * FROM Ativos;";

        try {
            st = conn.prepareStatement(select_statement);

            rs = st.executeQuery();

            while (rs.next()) {
                Asset asset = new Asset();

                UserDAO user = DaoFactory.createUserDAO();
                CategoryDAO categoryDAO = DaoFactory.createCategoryDAO();

                asset.setId(rs.getInt("id"));
                asset.setDescricao(rs.getString("descricao"));
                asset.setCategoria(categoryDAO.findById(rs.getInt("categoria")));
                asset.setNumeroSerie(rs.getString("numero_serie"));
                asset.setNotaFiscal(rs.getString("nota_fiscal"));
                asset.setModelo(rs.getString("modelo"));
                asset.setImagem(rs.getString("imagem"));
                asset.setValor(rs.getDouble("valor"));
                asset.setStatus(Status.valueOf(rs.getString("status")));
                asset.setObservacao(rs.getString("observacao"));
                asset.setCadastradoPor(user.findById(rs.getInt("cadastrado_por")));
                asset.setAlteradoPor(user.findById(rs.getInt("alterado_por")));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    asset.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    asset.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }

                assets.add(asset);
            }

            return assets;
        } catch (SQLException e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
            e.printStackTrace();
        } finally {
            DB.closeStatement(st);
            DB.closeResultset(rs);
        }

        return null;
    }

    @Override
    public Asset findById(Integer id) {
        Asset asset = new Asset();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Ativos WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {

                UserDAO user = DaoFactory.createUserDAO();
                CategoryDAO categoryDAO = DaoFactory.createCategoryDAO();

                asset.setId(id);
                asset.setDescricao(rs.getString("descricao"));
                asset.setCategoria(categoryDAO.findById(rs.getInt("categoria")));
                asset.setNumeroSerie(rs.getString("numero_serie"));
                asset.setNotaFiscal(rs.getString("nota_fiscal"));
                asset.setModelo(rs.getString("modelo"));
                asset.setImagem(rs.getString("imagem"));
                asset.setValor(rs.getDouble("valor"));
                asset.setStatus(Status.valueOf(rs.getString("status")));
                asset.setObservacao(rs.getString("observacao"));
                asset.setCadastradoPor(user.findById(rs.getInt("cadastrado_por")));
                asset.setAlteradoPor(user.findById(rs.getInt("alterado_por")));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    asset.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    asset.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }

            }

            return asset;
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
