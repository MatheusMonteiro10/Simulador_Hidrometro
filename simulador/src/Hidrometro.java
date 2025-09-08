class Hidrometro {
    private final Input input;
    private final Medidor medidor;
    private final Display display;
    //private final Output output;

    public Hidrometro(Input input, Medidor medidor, Display display) {
        this.input = input;
        this.medidor = medidor;
        this.display = display;
        //this.output = output;
    }

    public void ciclo() {
// Atualiza pressão baseada nos valores atuais (já calculada em Input quando setados)
        double vazao = input.getVolumeComAr() / 1_000_000.0; //m^3 > cm^3
        medidor.medir(vazao);
        display.apresentar(medidor.getAcumuladoM3(), input.getPressao());
    }
}