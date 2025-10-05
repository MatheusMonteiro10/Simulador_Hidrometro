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
        System.out.println("  setVolume [id] [volume]  -> altera o volume de um simulador");
        System.out.println("  setBitola [id] [tam]  -> altera a bitola de um simulador");
        System.out.println("  pause [id]         -> pausa um simulador");
        System.out.println("  resume [id]        -> retoma um simulador");
        System.out.println("  exit               -> encerra o programa\n");

        while (true) {
            System.out.print("> ");
            String cmd = sc.next();

            if (cmd.equalsIgnoreCase("exit")) {
                System.out.println("Encerrando simulação...");
                // encerra todos os simuladores ativos
                for (HidrometroRunnable sim : simuladores) {
                    if (sim != null) sim.stop();
                }
                System.exit(0);
            }
            else if (cmd.equalsIgnoreCase("start")) {
                int id = sc.nextInt();
                if (id >= 1 && id <= MAX_SIMULADORES) {
                    if (simuladores.get(id - 1) == null) {
                        HidrometroRunnable sim = new HidrometroRunnable(id, MATRICULA);
                        simuladores.set(id - 1, sim);
                        Thread t = new Thread(sim, "Simulador-" + id);
                        threads.set(id - 1, t);
                        t.start();
                        System.out.println("Simulador " + id + " iniciado.");
                    } else {
                        System.out.println("Simulador " + id + " já está ativo.");
                    }
                }
            }
            else if (cmd.equalsIgnoreCase("setVolume")) {
                int id = sc.nextInt();
                double volume = sc.nextDouble();
                if (id >= 1 && id <= MAX_SIMULADORES && simuladores.get(id - 1) != null) {
                    simuladores.get(id - 1).setVolume(volume);
                }
            }
            else if (cmd.equalsIgnoreCase("setBitola")) {
                int id = sc.nextInt();
                double bit = sc.nextDouble();
                if (id >= 1 && id <= MAX_SIMULADORES && simuladores.get(id - 1) != null) {
                    simuladores.get(id - 1).setBitola(bit);
                }
            }
            else if (cmd.equalsIgnoreCase("pause")) {
                int id = sc.nextInt();
                if (id >= 1 && id <= MAX_SIMULADORES && simuladores.get(id - 1) != null) {
                    simuladores.get(id - 1).pause();
                }
            }
            else if (cmd.equalsIgnoreCase("resume")) {
                int id = sc.nextInt();
                if (id >= 1 && id <= MAX_SIMULADORES && simuladores.get(id - 1) != null) {
                    simuladores.get(id - 1).resume();
                }
            }
            else {
                System.out.println("Comando inválido.");
                sc.nextLine(); // limpa entrada
            }
        }
    }
}
