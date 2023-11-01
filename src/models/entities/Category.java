package models.entities;

import java.time.LocalDateTime;

public class Category {
    private int id;
    private String descricao;
    private int vidaUtil;
    private User cadastradoPor;
    private User alteradoPor;
    private LocalDateTime cadastradoEm;
    private LocalDateTime alteradoEm;

    public Category() {
    }

    public Category(int id, String descricao, int vidaUtil) {
        this.id = id;
        this.descricao = descricao;
        this.vidaUtil = vidaUtil;
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

    public int getVidaUtil() {
        return vidaUtil;
    }

    public void setVidaUtil(int vidaUtil) {
        this.vidaUtil = vidaUtil;
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
        Category other = (Category) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
