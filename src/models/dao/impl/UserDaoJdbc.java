package models.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import gui.util.Alerts;
import javafx.scene.control.Alert.AlertType;
import models.dao.UserDAO;
import models.entities.User;
import models.enums.Perfil;
import models.enums.Status;

public class UserDaoJdbc implements UserDAO {

    private Connection conn = null;

    public UserDaoJdbc(Connection connection) {
        this.conn = connection;
    }

    private boolean existsUser(User user) {

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Usuarios WHERE usuario = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setString(1, user.getUsuario().toLowerCase());

            rs = st.executeQuery();

            if (rs.next()) {
                Alerts.showAlert("Atenção!", "Usuário " + user.getUsuario() + " já existe!",
                        "Verifique o login de usuário e tente novamente.", AlertType.WARNING);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void insert(User obj) {
        PreparedStatement st = null;
        String sql_statement = """
                INSERT INTO Usuarios (
                    nome,
                    usuario,
                    senha,
                    status,
                    perfil
                ) VALUES (
                    ?, ?, ?, ?, ?
                );
                """;

        // Consultar se já existe aquele usuario
        if (!existsUser(obj)) {
            try {
                st = conn.prepareStatement(sql_statement, Statement.RETURN_GENERATED_KEYS);

                st.setString(1, obj.getNome());
                st.setString(2, obj.getUsuario().toLowerCase());
                st.setString(3, obj.getSenha());
                st.setString(4, Status.ATIVO.name());
                st.setString(5, obj.getPerfil().name());

                int rowsAffected = st.executeUpdate();

                if (rowsAffected > 0) {
                    Statement stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");

                    if (rs.next()) {
                        int id = rs.getInt(1);
                        obj.setId(id);
                        Alerts.showAlert("Sucesso!", "Cadastro efetuado com sucesso!",
                                "Usuário " + obj.getNome() + " cadastrado.", AlertType.INFORMATION);
                    }
                    DB.closeResultset(rs);
                } else {
                    throw new SQLException("Unexpected erros: None rows affected!");
                }

            } catch (Exception e) {
                e.printStackTrace();
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
    public List<User> findAll() {
        List<User> usuarios = new ArrayList<>();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Usuarios;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            rs = st.executeQuery();

            while (rs.next()) {
                User aux = new User();

                aux.setId(rs.getInt("id"));
                aux.setNome(rs.getString("nome"));
                aux.setUsuario(rs.getString("usuario"));
                aux.setSenha(rs.getString("senha"));
                aux.setStatus(Status.valueOf(rs.getString("status")));
                aux.setPerfil(Perfil.valueOf(rs.getString("perfil")));

                usuarios.add(aux);
            }

            return usuarios;
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
    public User findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Usuarios WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setInt(1, id);

            rs = st.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(id);
                user.setNome(rs.getString("nome"));
                user.setUsuario(rs.getString("usuario"));
                user.setSenha(rs.getString("senha"));
                user.setStatus(Status.valueOf(rs.getString("status")));
                user.setPerfil(Perfil.valueOf(rs.getString("perfil")));
                return user;
            }

            return null;
        } catch (Exception e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
            return null;
        } finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        String sql_statement = """
                DELETE FROM Usuarios WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement);

            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro deletado com sucesso!",
                        "Usuário deletado.", AlertType.INFORMATION);
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
    public void update(User obj) {
        PreparedStatement st = null;
        String sql_statement = """
                UPDATE Usuarios SET
                    nome = ?,
                    usuario = ?,
                    senha = ?,
                    status = ?,
                    perfil = ?
                WHERE id = ?;
                """;

        try {
            st = conn.prepareStatement(sql_statement);

            st.setString(1, obj.getNome());
            st.setString(2, obj.getUsuario().toLowerCase());
            st.setString(3, obj.getSenha());
            st.setString(4, obj.getStatus().name());
            st.setString(5, obj.getPerfil().name());
            st.setInt(6, obj.getId());

            // System.out.println(st);

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                Alerts.showAlert("Sucesso!", "Cadastro alterado com sucesso!",
                        "Usuário " + obj.getNome() + " alterado.", AlertType.INFORMATION);
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
    public User findByUser(String searchedUser) {
        User user = new User();

        PreparedStatement st = null;
        ResultSet rs = null;
        String select_statement = """
                SELECT * FROM Usuarios u WHERE u.usuario = ?;
                    """;

        try {
            st = conn.prepareStatement(select_statement);

            st.setString(1, searchedUser.toLowerCase());

            rs = st.executeQuery();

            if (rs.next()) {
                user = new User();

                user.setId(rs.getInt("id"));
                user.setNome(rs.getString("nome"));
                user.setUsuario(rs.getString("usuario"));
                user.setSenha(rs.getString("senha"));
                user.setStatus(Status.valueOf(rs.getString("status")));
                user.setPerfil(Perfil.valueOf(rs.getString("perfil")));
            }

            return user;
        } catch (Exception e) {
            Alerts.showAlert(
                    "SQL Exception",
                    "Error in DataBase",
                    e.getMessage(),
                    AlertType.ERROR);
            return null;
        } finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
        }
    }

}
