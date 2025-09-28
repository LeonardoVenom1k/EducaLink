package br.com.javafx.educalink.areaalu;

import br.com.javafx.educalink.areaprof.Material;

import java.time.LocalDateTime;
import java.util.Objects;

public class Entrega {
    private String alunoMatricula;
    private String alunoNome;
    private Material atividade; // referência para a atividade original
    private String respostaTexto; // conteúdo do TextArea
    private String arquivoPath;   // caminho do arquivo anexado pelo aluno
    private LocalDateTime dataEntrega;
    private boolean corrigida = false;

    // 🔥 novos atributos de correção
    private String correcaoComentario;
    private String correcaoArquivo;

    public Entrega() {}

    public Entrega(String alunoMatricula, String alunoNome, Material atividade,
                   String respostaTexto, String arquivoPath, LocalDateTime dataEntrega) {
        this.alunoMatricula = alunoMatricula;
        this.alunoNome = alunoNome;
        this.atividade = atividade;
        this.respostaTexto = respostaTexto;
        this.arquivoPath = arquivoPath;
        this.dataEntrega = dataEntrega;
    }

    // ---------- Getters ----------
    public String getAlunoMatricula() { return alunoMatricula; }
    public String getAlunoNome() { return alunoNome; }
    public Material getAtividade() { return atividade; }
    public String getRespostaTexto() { return respostaTexto; }
    public String getArquivoPath() { return arquivoPath; }
    public LocalDateTime getDataEntrega() { return dataEntrega; }

    public String getCorrecaoComentario() { return correcaoComentario; }
    public String getCorrecaoArquivo() { return correcaoArquivo; }

    // ---------- Setters ----------
    public void setAlunoMatricula(String alunoMatricula) { this.alunoMatricula = alunoMatricula; }
    public void setAlunoNome(String alunoNome) { this.alunoNome = alunoNome; }
    public void setAtividade(Material atividade) { this.atividade = atividade; }
    public void setRespostaTexto(String respostaTexto) { this.respostaTexto = respostaTexto; }
    public void setArquivoPath(String arquivoPath) { this.arquivoPath = arquivoPath; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }
    public void setCorrigida(boolean corrigida) {
        this.corrigida = corrigida;
    }

    public void setCorrecaoComentario(String correcaoComentario) { this.correcaoComentario = correcaoComentario; }
    public void setCorrecaoArquivo(String correcaoArquivo) { this.correcaoArquivo = correcaoArquivo; }

    // ---------- Métodos auxiliares ----------
    public boolean temArquivoAnexado() {
        return arquivoPath != null && !arquivoPath.isBlank();
    }

    public boolean foiCorrigida() {
        return (correcaoComentario != null && !correcaoComentario.isBlank()) || (correcaoArquivo != null);
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "alunoMatricula='" + alunoMatricula + '\'' +
                ", alunoNome='" + alunoNome + '\'' +
                ", atividade=" + (atividade != null ? atividade.getAssunto() : "null") +
                ", respostaTexto='" + respostaTexto + '\'' +
                ", arquivoPath='" + arquivoPath + '\'' +
                ", dataEntrega=" + dataEntrega +
                ", correcaoComentario='" + correcaoComentario + '\'' +
                ", correcaoArquivo='" + correcaoArquivo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrega)) return false;
        Entrega entrega = (Entrega) o;
        return Objects.equals(alunoMatricula, entrega.alunoMatricula) &&
                Objects.equals(atividade, entrega.atividade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alunoMatricula, atividade);
    }
}