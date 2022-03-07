package com.example.com.tannerapp;

public class Cad_paciente {
    public String nome;
    public String data_nascimento;
    public String sexo;
    public String id_medico;
    public String email;
    public String responsavel;
    public String uuid;

    public Cad_paciente(String nome, String data_nascimento, String sexo, String id_medico, String email, String responsavel, String uuid){
        this.nome = nome;
        this.data_nascimento = data_nascimento;
        this.sexo = sexo;
        this.id_medico = id_medico;
        this.email = email;
        this.responsavel = responsavel;
        this.uuid = uuid;

    }

//    public Cad_paciente(String nome, String data_nascimento, String sexo, String hip_diagnostica, String inicio_tratamento, String fim_tratamento, String id_medico, String email, String uuid){
//        this.nome = nome;
//        this.data_nascimento = data_nascimento;
//        this.sexo = sexo;
//        this.hip_diagnostica = hip_diagnostica;
//        this.inicio_tratamento = inicio_tratamento;
//        this.fim_tratamento = fim_tratamento;
//        this.id_medico = id_medico;
//        this.email = email;
//        this.uuid = uuid;
//    }

    public String getNome() {
        return nome;
    }
}
