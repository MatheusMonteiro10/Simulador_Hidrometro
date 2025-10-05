import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Run {
    public static void main(String[] args) {
        final String MATRICULA = "202111250035";
        final int MAX_SIMULADORES = 5;

        List<HidrometroRunnable> simuladores = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        // preenche listas com null para ocupar posições
        for (int i = 0; i < MAX_SIMULADORES; i++) {
            simuladores.add(null);
            threads.add(null);
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== Controle dos Simuladores de Hidrômetro ===");
        System.out.println("Comandos disponíveis:");
        System.out.println("  start [id]         -> inicia um simulador (1-5)");
        System.out.println("  setvolume [id] [volume]  -> altera o volume de um simulador");
        System.out.println("  setbitola [id] [tam]  -> altera a bitola de um simulador");
        System.out.println("  pause [id]         -> pausa um simulador");
        System.out.println("  resume [id]        -> retoma um simulador");
        System.out.println("  exit               -> encerra o programa\n");

        while (true) {
            System.out.print("> ");
            String cmd = sc.next().toLowerCase();

            switch(cmd) {
                case "exit":
                    System.out.println("Encerrando simulação...");
                    // encerra todos os simuladores ativos
                    for (HidrometroRunnable sim : simuladores) {
                        if (sim != null) sim.stop();
                    }
                    System.exit(0);
                    break;

                case "start":
                    int idStart = sc.nextInt();
                    if (idStart >= 1 && idStart <= MAX_SIMULADORES) {
                        if (simuladores.get(idStart - 1) == null) {
                            HidrometroRunnable sim = new HidrometroRunnable(idStart, MATRICULA);
                            simuladores.set(idStart - 1, sim);
                            Thread t = new Thread(sim, "Simulador-" + idStart);
                            threads.set(idStart - 1, t);
                            t.start();
                            System.out.println("Simulador " + idStart + " iniciado.");
                        } else {
                            System.out.println("Simulador " + idStart + " já está ativo.");
                        }
                    }
                    break;

                case "setvolume":
                    int idVolume = sc.nextInt();
                    double volume = sc.nextDouble();
                    if (idVolume >= 1 && idVolume <= MAX_SIMULADORES && simuladores.get(idVolume - 1) != null) {
                        simuladores.get(idVolume - 1).setVolume(volume);
                    }
                    break;

                case "setbitola":
                    int id = sc.nextInt();
                    double bit = sc.nextDouble();
                    if (id >= 1 && id <= MAX_SIMULADORES && simuladores.get(id - 1) != null) {
                        simuladores.get(id - 1).setBitola(bit);
                    }
                    break;

                case "pause":
                    int idPause = sc.nextInt();
                    if (idPause >= 1 && idPause <= MAX_SIMULADORES && simuladores.get(idPause - 1) != null) {
                        simuladores.get(idPause - 1).pause();
                    }
                    break;

                case "resume":
                    int idResume = sc.nextInt();
                    if (idResume >= 1 && idResume <= MAX_SIMULADORES && simuladores.get(idResume - 1) != null) {
                        simuladores.get(idResume - 1).resume();
                    }
                    break;

                default:
                    System.out.println("Comando inválido.");
                    sc.nextLine(); // limpa entrada
                    break;
            }
        }
    }
}
