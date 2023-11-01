package controllers.helpers;

import controllers.categories.EditCategoriasViewController;
import models.entities.Category;

public class CategoriasHelper {
    private EditCategoriasViewController controller;

    public CategoriasHelper(EditCategoriasViewController editCategoriasViewController) {
        this.controller = editCategoriasViewController;
    }

    public void setCategoria(Category categoria) {
        controller.getTxtDescricao().setText(categoria.getDescricao());
        controller.getTxtVidaUtil().setText(String.valueOf(categoria.getVidaUtil()));
    }

    public Category getCategoria() {
        Category categoria = new Category();

        categoria.setDescricao(controller.getTxtDescricao().getText());
        categoria.setVidaUtil(Integer.parseInt(controller.getTxtVidaUtil().getText()));

        return categoria;
    }

    public void limpar() {
        controller.getTxtDescricao().setText("");
        controller.getTxtVidaUtil().setText("");
    }

}
