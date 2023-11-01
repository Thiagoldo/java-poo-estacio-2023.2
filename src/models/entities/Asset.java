package models.entities;

import java.time.LocalDateTime;

import models.enums.Status;

public class Asset {
    private Integer id;
    private String descricao;
    private Category categoria;
    private String numeroSerie;
    private String notaFiscal;
    private String modelo;
    private String imagem;
    private Double valor;
    private Status status;
    private String observacao;
    private User cadastradoPor;
    private User alteradoPor;
    private LocalDateTime cadastradoEm;
    private LocalDateTime alteradoEm;

    public Asset() {
    }

    public Asset(int id, String descricao, Category categoria, double valor, Status status) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Category getCategoria() {
        return categoria;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public User getCadastradoPor() {
        return cadastradoPor;
    }

    public void setCadastradoPor(User cadastradoPor) {
        this.cadastradoPor = cadastradoPor;
    }

    public User getAlteradoPor() {
        return alteradoPor;
    }

    public void setAlteradoPor(User alteradoPor) {
        this.alteradoPor = alteradoPor;
    }

    public LocalDateTime getCadastradoEm() {
        return cadastradoEm;
    }

    public void setCadastradoEm(LocalDateTime cadastradoEm) {
        this.cadastradoEm = cadastradoEm;
    }

    public LocalDateTime getAlteradoEm() {
        return alteradoEm;
    }

    public void setAlteradoEm(LocalDateTime alteradoEm) {
        this.alteradoEm = alteradoEm;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Asset other = (Asset) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
