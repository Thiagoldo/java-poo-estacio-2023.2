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
import models.dao.DaoFactory;
import models.dao.SectorDAO;
import models.dao.UserDAO;
import models.entities.Sector;

public class SectorDaoJdbc implements SectorDAO {

    private Connection conn = null;

    public SectorDaoJdbc(Connection connection) {
        this.conn = connection;
    }

    private boolean existsSector(Sector sector) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Setores WHERE descricao = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setString(1, sector.getDescricao());

            rs = st.executeQuery();

            if (rs.next()) {
                Alerts.showAlert("Atenção!", "Setor " + sector.getDescricao() + " já existe!",
                        "Verifique a descrição do setor e tente novamente.", AlertType.WARNING);
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
    public void insert(Sector obj) {

        PreparedStatement st = null;
        String sql_statement = """
                INSERT INTO Setores (
                    responsavel,
                    cadastrado_por,
                    cadastrado_em,
                    descricao
                ) VALUES (
                    ?, ?, ?, ?
                );
                """;

        // Consultar se já existe aquele usuario
        if (!existsSector(obj)) {
            try {
                st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

                st.setInt(1, obj.getResponsavel().getId());
                st.setInt(2, App.getAUTHENTICATED_USER().getId());

                LocalDateTime now = LocalDateTime.now();

                st.setTimestamp(3, Timestamp.valueOf(now));
                st.setString(4, obj.getDescricao());

                int rowsAffected = st.executeUpdate();

                if (rowsAffected > 0) {
                    Statement stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        obj.setId(id);
                        obj.setCadastradoEm(now);
                        Alerts.showAlert("Sucesso!", "Cadastro efetuado com sucesso!",
                                "Setor " + obj.getDescricao() + " cadastrado.", AlertType.INFORMATION);
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
    public void update(Sector obj) {
        PreparedStatement st = null;
        String sql_statement = """
                UPDATE Setores SET
                    responsavel = ?,
                    alterado_por = ?,
                    alterado_em = ?,
                    descricao = ?
                WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, obj.getResponsavel().getId());
            st.setInt(2, App.getAUTHENTICATED_USER().getId());

            LocalDateTime now = LocalDateTime.now();

            st.setTimestamp(3, Timestamp.valueOf(now));
            st.setString(4, obj.getDescricao());
            st.setInt(5, obj.getId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro alterado com sucesso!",
                        "Setor " + obj.getDescricao() + " alterado.", AlertType.INFORMATION);
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
                DELETE FROM Setores WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement);

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro deletado com sucesso!",
                        "Setor deletado.", AlertType.INFORMATION);
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
    public List<Sector> findAll() {
        List<Sector> setores = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Setores;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            rs = st.executeQuery();

            while (rs.next()) {
                Sector aux = new Sector();
                UserDAO user = DaoFactory.createUserDAO();

                aux.setId(rs.getInt("id"));
                aux.setResponsavel(user.findById(rs.getInt("responsavel")));
                aux.setCadastradoPor(user.findById(rs.getInt("cadastrado_por")));
                aux.setAlteradoPor(user.findById(rs.getInt("alterado_por")));
                aux.setDescricao(rs.getString("descricao"));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    aux.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    aux.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }

                setores.add(aux);
            }

            return setores;
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
    public Sector findById(Integer id) {
        Sector setor = new Sector();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Setores WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {

                UserDAO user = DaoFactory.createUserDAO();

                setor.setId(rs.getInt("id"));
                setor.setResponsavel(user.findById(rs.getInt("responsavel")));
                setor.setCadastradoPor(user.findById(rs.getInt("cadastrado_por")));
                setor.setAlteradoPor(user.findById(rs.getInt("alterado_por")));
                setor.setDescricao(rs.getString("descricao"));
                if (rs.getTimestamp("cadastrado_em") != null) {
                    setor.setCadastradoEm(rs.getTimestamp("cadastrado_em").toLocalDateTime());
                }
                if (rs.getTimestamp("alterado_em") != null) {
                    setor.setAlteradoEm(rs.getTimestamp("alterado_em").toLocalDateTime());
                }

            }

            return setor;
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