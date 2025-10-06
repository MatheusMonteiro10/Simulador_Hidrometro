import java.util.Random;
import static java.lang.Math.clamp;
import static java.lang.Math.pow;

public class Input {

    private double tamEntradaCano; // bitola
    private double volume; // cm^3
    private double pressao; // bars

    public static final double MIN_VOLUMEM3 = 0.0; // Valor mínimo de entrada de água em cm^3
    public static final double MAX_VOLUMEM3 = 100.0; // Valor máximo de entrada de água em cm^3
    public static final double MIN_BITOLA_MM = 10.0; // Tamanho mínimo da entrada do cano em milimetros
    public static final double MAX_BITOLA_MM = 100.0; // Tamanho máximo da entrada do cano em milimetros

    private final Random random = new Random();

    public Input(double tamEntradaCano, double volume) {
        //se tam fornecido estiver entre os limites ele permancece, se não estiver, fica valendo um dos limites
        this.tamEntradaCano = clamp(tamEntradaCano, MIN_BITOLA_MM, MAX_BITOLA_MM);
        this.volume = clamp(volume, MIN_VOLUMEM3, MAX_VOLUMEM3);
        //pressao = volume/bitola^2
        this.pressao = volume / (pow(tamEntradaCano,2));
    }

    public synchronized void setVolume(double volume) {
        this.volume = clamp(volume, MIN_VOLUMEM3, MAX_VOLUMEM3);
        atualizarPressao();
    }

    public synchronized void setTamEntradaCano(double tamEntradaCano) {
        this.tamEntradaCano = clamp(tamEntradaCano, MIN_BITOLA_MM, MAX_BITOLA_MM);
        atualizarPressao();
    }

    public synchronized double getVolume() {
        return volume;
    }
    public synchronized double getTamEntradaCano() {
        return tamEntradaCano;
    }
    public synchronized double getPressao() {
        return pressao;
    }

    public synchronized double getVolumeComAr() {
        final double fluxoDeAr = 0.01; // m^3 por ciclo quando volume == 0
        return (volume <= 0.0) ? fluxoDeAr : volume;
    }

    private synchronized void atualizarPressao() {
        double p = (tamEntradaCano > 0) ? volume / (pow(tamEntradaCano, 2)) : 0.0;
        this.pressao = clamp(p, 0.0, 16.0); //máximo de 16 bars
    }

    // gera um valor aleatório para volume entre o máximo e o mínimo permitido
    public synchronized void randomizarVolume() {
        setVolume(MIN_VOLUMEM3 + random.nextDouble() * (MAX_VOLUMEM3 - MIN_VOLUMEM3));
    }

}
