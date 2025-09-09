public class Run{
    public static void main(String[] args) throws Exception {
        // bitola = 30mm e volume = 10 m^3
        Input input = new Input(30.0, 10.0);
        Medidor medidor = new Medidor();
        Display display = new Display();

        // executa o ciclo
        Hidrometro hidrometro = new Hidrometro(input, medidor, display);
        hidrometro.executarCiclo();
    }
}
