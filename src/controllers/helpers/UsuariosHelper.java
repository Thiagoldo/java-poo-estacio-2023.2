package controllers.helpers;

import controllers.users.EditUsuariosViewController;
import models.entities.User;

public class UsuariosHelper {
    private EditUsuariosViewController controller;

    public UsuariosHelper(EditUsuariosViewController controller) {
        this.controller = controller;
    }

    public void setUser(User user) {

        controller.getTxtNome().setText(user.getNome());
        controller.getTxtUsuario().setText(user.getUsuario());
        controller.getTxtSenha().setText(user.getSenha());
        controller.getcBoxPerfil().getSelectionModel().select(user.getPerfil());
        controller.getcBoxStatus().getSelectionModel().select(user.getStatus());

    }

    public User getUser() {
        User user = new User();

        user.setNome(controller.getTxtNome().getText());
        user.setUsuario(controller.getTxtUsuario().getText());
        user.setSenha(controller.getTxtSenha().getText());
        user.setPerfil(controller.getcBoxPerfil().getSelectionModel().getSelectedItem());
        user.setStatus(controller.getcBoxStatus().getSelectionModel().getSelectedItem());

        return user;
    }

    public void limpar() {
        controller.getTxtNome().setText("");
        controller.getTxtUsuario().setText("");
        controller.getTxtSenha().setText("");
        controller.getcBoxPerfil().getSelectionModel().selectFirst();
        controller.getcBoxStatus().getSelectionModel().selectFirst();
    }
}
