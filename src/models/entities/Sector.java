package models.entities;

import java.time.LocalDateTime;

public class Sector {
    private int id;
    private String descricao;
    private User responsavel;
    private User cadastradoPor;
    private User alteradoPor;
    private LocalDateTime cadastradoEm;
    private LocalDateTime alteradoEm;

    public Sector() {
    }

    public Sector(String descricao, User responsavel, User cadastradoPor, User alteradoPor, LocalDateTime cadastradoEm,
            LocalDateTime alteradoEm) {
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.cadastradoPor = cadastradoPor;
        this.alteradoPor = alteradoPor;
        this.cadastradoEm = cadastradoEm;
        this.alteradoEm = alteradoEm;
    }

    public Sector(int id, String descricao, User responsavel, User cadastradoPor, User alteradoPor,
            LocalDateTime cadastradoEm, LocalDateTime alteradoEm) {
        this.id = id;
        this.descricao = descricao;
        this.responsavel = responsavel;
        this.cadastradoPor = cadastradoPor;
        this.alteradoPor = alteradoPor;
        this.cadastradoEm = cadastradoEm;
        this.alteradoEm = alteradoEm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public User getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(User responsavel) {
        this.responsavel = responsavel;
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
        Sector other = (Sector) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
