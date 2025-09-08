public class Teste {

    public static void main(String[] args) throws Exception {
        // Cria Input com bitola = 50mm e volume = 10 m³
        Input entrada = new Input(50.0, 10.0);
        Medidor medidor = new Medidor();
        Display display = new Display();

        // Simula 15 ciclos
        for (int i = 0; i < 15; i++) {
            // Medição
            medidor.medir(entrada.getVolume());

            // Mostra no display
            display.apresentar(medidor.getAcumuladoM3(), entrada.getPressao());

            // Exibe no console também
            System.out.printf("Ciclo %d -> Volume=%.2f | Bitola=%.2f | Pressão=%.2f | Acumulado=%.2f%n",
                    i+1, entrada.getVolume(), entrada.getTamEntradaCano(), entrada.getPressao(), medidor.getAcumuladoM3());

            // Simula alteração do volume (a cada iteração aumenta um pouco)
            entrada.setVolume(Math.random() * Input.MAX_VOLUMEM3);

            // Espera 1 segundo entre ciclos
            Thread.sleep(1000);
        }
    }

}
