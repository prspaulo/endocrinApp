package com.example.com.tannerapp;

public class Consulta {

    public String uuid_consulta;
    public double escala_mama;
    public double escala_pelo;
    public double altura;
    public double peso;
    public String nome_paciente;
    public String data_consulta;
    public double idade_atual;
    public String idade_menarca;
    public String uuid_paciente;
    public String dtIniTratamento;
    public String dtFimTratamento;

    public Consulta(String uuid_consulta, double escala_mama, double escala_pelo, double altura, double peso, String nome_paciente, String data_consulta, double idade_atual, String idade_menarca, String uuid_paciente, String dtIniTratamento, String dtFimTratamento){
        this.uuid_consulta = uuid_consulta;
        this.escala_mama = escala_mama;
        this.escala_pelo = escala_pelo;
        this.altura = altura;
        this.peso = peso;
        this.nome_paciente = nome_paciente;
        this.data_consulta = data_consulta;
        this.idade_atual = idade_atual;
        this.idade_menarca = idade_menarca;
        this.uuid_paciente = uuid_paciente;
        this.dtIniTratamento = dtIniTratamento;
        this.dtFimTratamento = dtFimTratamento;
    }

    public Consulta(String uuid_consulta, double altura, double peso, String nome_paciente, String data_consulta, double idade_atual, String idade_menarca, String uuid_paciente, String dtIniTratamento, String dtFimTratamento){
        this.uuid_consulta = uuid_consulta;
        this.altura = altura;
        this.peso = peso;
        this.nome_paciente = nome_paciente;
        this.data_consulta = data_consulta;
        this.idade_atual = idade_atual;
        this.idade_menarca = idade_menarca;
        this.uuid_paciente = uuid_paciente;
        this.dtIniTratamento = dtIniTratamento;
        this.dtFimTratamento = dtFimTratamento;
    }

    public String get_uuid_consulta() {

        return uuid_consulta;
    }

    public double getEscala_mama() {

        return escala_mama;
    }

    public double getEscala_pelo() {

        return escala_pelo;
    }

    public double getPeso() {

        return peso;
    }

    public double getAltura() {
        return altura;
    }

    public String getNome_paciente() {
        return nome_paciente;
    }

    public String getData_consulta() {
        return data_consulta;
    }

    public double getIdade_atual() {
        return idade_atual;
    }

    public String getUuid_paciente() {
        return uuid_paciente;
    }

    public String getIdade_menarca() { return idade_menarca; }

    public String getDtIniTratamento() { return dtIniTratamento; }

    public String getDtFimTratamento() { return dtFimTratamento; }

}
