public class Run {

    public static void main(String[] args) throws Exception {
        // Valores iniciais (podem ser alterados externamente ou randomizados)
        Input input = new Input(50.0, 10.0); // bitola 50mm, 10 m^3 por ciclo
        Medidor medidor = new Medidor();
        Display display = new Display();

        Hidrometro hidrometro = new Hidrometro(input, medidor, display);

        // Loop contínuo — tal qual um equipamento real.
        // Para simular variabilidade "externa" em bitola/volume, randomizamos periodicamente.
        long iter = 0;
        while (true) {
            // A cada 20 ciclos, variamos a vazão (0–100 m^3)
            if (iter % 15 == 0) {
                input.randomizarVolume();
            }

            // A cada 120 ciclos, variamos a bitola (10–100 mm)
            //if (iter % 120 == 0) {
              //  input.randomizarBitola();
            //}

            hidrometro.ciclo();
            iter++;

            // Controle de ritmo da simulação
            Thread.sleep(300); // 0,3 s por ciclo
        }
    }

}