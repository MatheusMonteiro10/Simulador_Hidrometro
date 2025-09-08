public class Hidrometro {
    private final Input input;
    private final Medidor medidor;
    private final Display display;

    public Hidrometro(Input input, Medidor medidor, Display display) {
        this.input = input;
        this.medidor = medidor;
        this.display = display;
    }

    public void executarCiclo() throws InterruptedException {
        long iter = 0;

        // Simula ciclos, alterando apenas o volume
        while (true) {
            // A cada 15 ciclos, variamos o volume (mas a bitola permanece fixa)
            if (iter % 15 == 0) {
                input.randomizarVolume();  // Randomiza o volume
            }

            // Calcula o vazão em m³
            double vazao = input.getVolumeComAr() / 1_000.0; // Convertendo de cm³ para m³ (1_000_000.0 demora muito)

            // Atualiza o medidor com o vazão calculado
            medidor.medir(vazao);

            // Exibe as informações no display
            display.apresentar(medidor.getAcumuladoM3(), input.getPressao());

            // Exibe no console as informações
            System.out.printf("Ciclo %d -> Volume=%.2f | Bitola=%.2f | Pressão=%.2f | Acumulado=%.2f%n",
                    iter + 1, input.getVolume(), input.getTamEntradaCano(), input.getPressao(), medidor.getAcumuladoM3());

            // Incrementa o ciclo
            iter++;

            // Controle de ritmo da simulação
            Thread.sleep(300); // 0,3 s por ciclo
        }
    }
}
