package models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AssetSector {

    private Asset ativo;
    private Sector setor;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private String observacao;

    private User cadastradoPor;
    private User alteradoPor;

    private LocalDateTime cadastradoEm;
    private LocalDateTime alteradoEm;

    public AssetSector() {
    }

    public AssetSector(Asset ativo, Sector setor, LocalDate dataInicio, LocalDate dataFim, String observacao,
            User cadastradoPor, User alteradoPor, LocalDateTime cadastradoEm, LocalDateTime alteradoEm) {
        this.ativo = ativo;
        this.setor = setor;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.observacao = observacao;
        this.cadastradoPor = cadastradoPor;
        this.alteradoPor = alteradoPor;
        this.cadastradoEm = cadastradoEm;
        this.alteradoEm = alteradoEm;
    }

    public Asset getAtivo() {
        return ativo;
    }

    public void setAtivo(Asset ativo) {
        this.ativo = ativo;
    }

    public Sector getSetor() {
        return setor;
    }

    public void setSetor(Sector setor) {
        this.setor = setor;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
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
        result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
        result = prime * result + ((setor == null) ? 0 : setor.hashCode());
        result = prime * result + ((dataInicio == null) ? 0 : dataInicio.hashCode());
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
        AssetSector other = (AssetSector) obj;
        if (ativo == null) {
            if (other.ativo != null)
                return false;
        } else if (!ativo.equals(other.ativo))
            return false;
        if (setor == null) {
            if (other.setor != null)
                return false;
        } else if (!setor.equals(other.setor))
            return false;
        if (dataInicio == null) {
            if (other.dataInicio != null)
                return false;
        } else if (!dataInicio.equals(other.dataInicio))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AssetSector [ativo=" + ativo + ", setor=" + setor + ", dataInicio=" + dataInicio + ", dataFim="
                + dataFim + ", observacao=" + observacao + ", cadastradoPor=" + cadastradoPor + ", alteradoPor="
                + alteradoPor + ", cadastradoEm=" + cadastradoEm + ", alteradoEm=" + alteradoEm + "]";
    }


}
