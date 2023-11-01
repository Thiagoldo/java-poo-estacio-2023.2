package controllers.helpers;

import controllers.sectors.EditSetoresViewController;
import models.entities.Sector;

public class SetoresHelper {
    private EditSetoresViewController controller;

    public SetoresHelper(EditSetoresViewController controller) {
        this.controller = controller;
    }

    public void setSetor(Sector setor) {
        controller.getTxtDescricao().setText(setor.getDescricao());
        controller.getcBoxResponsavel().getSelectionModel().select(setor.getResponsavel());
    }

    public Sector getSetor() {
        Sector setor = new Sector();

        setor.setDescricao(controller.getTxtDescricao().getText());
        setor.setResponsavel(controller.getcBoxResponsavel().getSelectionModel().getSelectedItem());

        return setor;
    }

    public void limpar() {
        controller.getTxtDescricao().setText("");
        controller.getcBoxResponsavel().getSelectionModel().selectFirst();
    }
}
