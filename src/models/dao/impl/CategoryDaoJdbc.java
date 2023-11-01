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
import models.dao.CategoryDAO;
import models.dao.DaoFactory;
import models.dao.UserDAO;
import models.entities.Category;

public class CategoryDaoJdbc implements CategoryDAO {

    private Connection conn = null;

    public CategoryDaoJdbc(Connection connection) {
        this.conn = connection;
    }

    private boolean existsCategory(Category category) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Categorias WHERE descricao = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setString(1, category.getDescricao());

            rs = st.executeQuery();

            if (rs.next()) {
                Alerts.showAlert("Atenção!", "Categoria " + category.getDescricao() + " já existe!",
                        "Verifique a descrição da categoria e tente novamente.", AlertType.WARNING);
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
    public void insert(Category obj) {

        PreparedStatement st = null;
        String sql_statement = """
                INSERT INTO Categorias (
                    descricao,
                    vida_util,
                    cadastrado_por,
                    cadastrado_em
                ) VALUES (
                    ?, ?, ?, ?
                );
                """;

        // Consultar se já existe aquele usuario
        if (!existsCategory(obj)) {
            try {
                st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

                st.setString(1, obj.getDescricao());
                st.setInt(2, obj.getVidaUtil());
                st.setInt(3, App.getAUTHENTICATED_USER().getId());

                LocalDateTime now = LocalDateTime.now();

                st.setTimestamp(4, Timestamp.valueOf(now));

                int rowsAffected = st.executeUpdate();

                if (rowsAffected > 0) {
                    Statement stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        obj.setId(id);
                        obj.setCadastradoEm(now);
                        Alerts.showAlert("Sucesso!", "Cadastro efetuado com sucesso!",
                                "Categoria " + obj.getDescricao() + " cadastrada.", AlertType.INFORMATION);
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
    }

    @Override
    public void update(Category obj) {
        PreparedStatement st = null;
        String sql_statement = """
                UPDATE Categorias SET
                    descricao = ?,
                    vida_util = ?,
                    alterado_por = ?,
                    alterado_em = ?
                WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getDescricao());
            st.setInt(2, obj.getVidaUtil());
            st.setInt(3, App.getAUTHENTICATED_USER().getId());
            
            LocalDateTime now = LocalDateTime.now();
            
            st.setTimestamp(4, Timestamp.valueOf(now));
            st.setInt(5, obj.getId());
            
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro alterado com sucesso!",
                        "Categoria " + obj.getDescricao() + " alterado.", AlertType.INFORMATION);
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
                DELETE FROM Categorias WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement);

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro deletado com sucesso!",
                        "Catergoria deletada.", AlertType.INFORMATION);
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
    public List<Category> findAll() {

        List<Category> categories = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = "SELECT * FROM Categorias;";

        try {
            st = conn.prepareStatement(select_statement);

            rs = st.executeQuery();

            while (rs.next()) {
                Category aux = new Category();

                UserDAO userDao = DaoFactory.createUserDAO();

                aux.setId(rs.getInt("id"));
                aux.setDescricao(rs.getString("descricao"));
                aux.setVidaUtil(rs.getInt("vida_util"));
                aux.setCadastradoPor(userDao.findById(rs.getInt("cadastrado_por")));
                aux.setAlteradoPor(userDao.findById(rs.getInt("alterado_por")));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    aux.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    aux.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }

                categories.add(aux);
            }

            return categories;
        } catch (SQLException e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
        } finally {
            DB.closeStatement(st);
            DB.closeResultset(rs);
        }

        return null;
    }

    @Override
    public Category findById(Integer id) {
        Category category = new Category();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Categorias WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {

                UserDAO user = DaoFactory.createUserDAO();

                category.setId(id);
                category.setDescricao(rs.getString("descricao"));
                category.setVidaUtil(rs.getInt("vida_util"));
                category.setCadastradoPor(user.findById(rs.getInt("cadastrado_por")));
                category.setAlteradoPor(user.findById(rs.getInt("alterado_por")));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    category.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    category.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }
            }

            return category;
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
