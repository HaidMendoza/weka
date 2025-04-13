package com.weka.wekaProyecto.entities;

public class Persona {
    private String nombre;
    private double age;
    private double plasmaGlucose;
    private double bloodPressure;
    private double skinThickness;
    private double insulin;
    private double bmi;
    private double diabetesPedigreeFunction;
    private double pregnancies;

    private String resultado;

    // 🔹 Getter y Setter de nombre
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getPlasmaGlucose() {
        return plasmaGlucose;
    }

    public void setPlasmaGlucose(double plasmaGlucose) {
        this.plasmaGlucose = plasmaGlucose;
    }

    public double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public double getSkinThickness() {
        return skinThickness;
    }

    public void setSkinThickness(double skinThickness) {
        this.skinThickness = skinThickness;
    }

    public double getInsulin() {
        return insulin;
    }

    public void setInsulin(double insulin) {
        this.insulin = insulin;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getDiabetesPedigreeFunction() {
        return diabetesPedigreeFunction;
    }

    public void setDiabetesPedigreeFunction(double diabetesPedigreeFunction) {
        this.diabetesPedigreeFunction = diabetesPedigreeFunction;
    }

    public double getPregnancies() {
        return pregnancies;
    }

    public void setPregnancies(double pregnancies) {
        this.pregnancies = pregnancies;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
