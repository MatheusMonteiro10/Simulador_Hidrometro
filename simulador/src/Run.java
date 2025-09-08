public class Run{
    public static void main(String[] args) throws Exception {
        // Cria Input com bitola = 50mm e volume = 10 m³
        Input input = new Input(30.0, 10.0);  // A bitola está fixada em 30mm
        Medidor medidor = new Medidor();
        Display display = new Display();

        // Cria o objeto Hidrometro e executa o ciclo
        Hidrometro hidrometro = new Hidrometro(input, medidor, display);
        hidrometro.executarCiclo();
    }
}
