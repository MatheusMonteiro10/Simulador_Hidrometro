class Medidor {
    private double acumuladoM3; // metros cúbicos consumidos no total

    public Medidor() {
        this.acumuladoM3 = 0.0;
    }

    // Calcula incremento de consumo a partir da vazão (m^3 por ciclo)
    public void medir(double vazaoM3PorCiclo) {
        if (vazaoM3PorCiclo < 0) vazaoM3PorCiclo = 0;
        this.acumuladoM3 += vazaoM3PorCiclo;
    }

    public double getAcumuladoM3() {
        return acumuladoM3;
    }
}