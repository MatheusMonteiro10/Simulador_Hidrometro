import java.util.Scanner;

public class Run {
    public static void main(String[] args) throws Exception {
        Input input = new Input(30.0, 10.0);
        Medidor medidor = new Medidor();
        Display display = new Display();
        Hidrometro hidrometro = new Hidrometro(input, medidor, display);

        // Thread da simulação
        Thread simulacao = new Thread(() -> {
            try {
                hidrometro.executarCiclo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        simulacao.start();

        // Thread de entrada do usuário
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Digite novo volume (cm³): ");
            if (sc.hasNextDouble()) {
                double novoVolume = sc.nextDouble();
                input.setVolume(novoVolume);
                System.out.println("Volume atualizado para " + novoVolume + " cm³");
            } else {
                sc.next(); // descarta entrada inválida
            }
        }
    }
}
