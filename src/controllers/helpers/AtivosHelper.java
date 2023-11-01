package controllers.helpers;

import java.time.format.DateTimeFormatter;

import controllers.assets.EditAtivosViewController;
import javafx.scene.image.Image;
import models.entities.Asset;

public class AtivosHelper {
    private EditAtivosViewController controller;
    DateTimeFormatter fmt;

    public AtivosHelper(EditAtivosViewController editAtivosViewController) {
        this.controller = editAtivosViewController;
        this.fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void setAtivo(Asset ativo) {
        controller.getTxtDescricao().setText(ativo.getDescricao());
        controller.getTxtNotaFiscal().setText(ativo.getNotaFiscal());
        controller.getTxtNumeroSerie().setText(ativo.getNumeroSerie());
        controller.getTxtModelo().setText(ativo.getModelo());
        controller.getTxtObservacao().setText(ativo.getObservacao());

        controller.getTxtDataCadastro().setText(ativo.getCadastradoEm().format(fmt));

        controller.getcBoxCategorias().getSelectionModel().select(ativo.getCategoria());
        controller.getcBoxStatus().getSelectionModel().select(ativo.getStatus());
        
        controller.getTxtValor().setText(String.valueOf(ativo.getValor()));

        if(ativo.getImagem() != null){
            controller.getImgAtivo().setImage(new Image(ativo.getImagem()));
        }
    }

    public Asset getAtivo() {
        Asset ativo = new Asset();

        ativo.setDescricao(controller.getTxtDescricao().getText());
        ativo.setCategoria(controller.getcBoxCategorias().getSelectionModel().getSelectedItem());
        ativo.setStatus(controller.getcBoxStatus().getSelectionModel().getSelectedItem());
        ativo.setNotaFiscal(controller.getTxtNotaFiscal().getText());
        ativo.setNumeroSerie(controller.getTxtNumeroSerie().getText());
        ativo.setModelo(controller.getTxtModelo().getText());
        ativo.setValor(Double.parseDouble(controller.getTxtValor().getText()));
        ativo.setImagem(controller.getImgAtivo().getImage().getUrl());
        ativo.setObservacao(controller.getTxtObservacao().getText());

        return ativo;
    }

    public void limpar() {
        controller.getTxtDescricao().setText("");
        controller.getTxtNotaFiscal().setText("");
        controller.getTxtNumeroSerie().setText("");
        controller.getTxtModelo().setText("");
        controller.getTxtObservacao().setText("");

        controller.getTxtDataCadastro().setText("");

        controller.getcBoxCategorias().getSelectionModel().selectFirst();
        controller.getcBoxStatus().getSelectionModel().selectFirst();
        
        controller.getTxtValor().setText("");

        controller.getImgAtivo().setImage(null);
    }

}
