package br.com.javafx.educalink.areaprof;

import java.io.Serializable;

public class Correcao implements Serializable {
    private String atividadeId;
    private String alunoMatricula;
    private String caminhoArquivo;
    private String comentario;
    private String materia;    // nova
    private String assunto;    // nova

    public Correcao(String atividadeId, String alunoMatricula, String caminhoArquivo, String comentario, String materia, String assunto) {
        this.atividadeId = atividadeId;
        this.alunoMatricula = alunoMatricula;
        this.caminhoArquivo = caminhoArquivo;
        this.comentario = comentario;
        this.materia = materia;
        this.assunto = assunto;
    }

    public String getAtividadeId() { return atividadeId; }
    public void setAtividadeId(String atividadeId) { this.atividadeId = atividadeId; }

    public String getAlunoMatricula() { return alunoMatricula; }
    public void setAlunoMatricula(String alunoMatricula) { this.alunoMatricula = alunoMatricula; }

    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }

    public String getAssunto() { return assunto; }
    public void setAssunto(String assunto) { this.assunto = assunto; }
}

