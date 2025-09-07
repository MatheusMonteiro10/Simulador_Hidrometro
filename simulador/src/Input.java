import java.util.Random;
import static java.lang.Math.clamp;
import static java.lang.Math.pow;

public class Input {

    private double tamEntradaCano; //Bitola
    private double volume;
    private double pressao;

    public static final double MIN_VOLUMEM3 = 0.0; //Valor mínimo de entrada de água em cm^3
    public static final double MAX_VOLUMEM3 = 100.0; //Valor máximo de entrada de água em cm^3

    public static final double MIN_BITOLA_MM = 10.0; //Tamanho mínimo da entrada do cano em milímetros
    public static final double MAX_BITOLA_MM = 100.0; //Tamanho máximo da entrada em milímetros

    private Random random = new Random();

    public Input(double tamEntradaCano, double volume) {
        //se tam fornecido estiver entre os limites ele permancece, se não estiver, fica valendo um dos limites
        this.tamEntradaCano = clamp(tamEntradaCano, MIN_BITOLA_MM, MAX_BITOLA_MM);
        this.volume = clamp(volume, MIN_VOLUMEM3, MAX_VOLUMEM3);
        pressao = volume / (pow(tamEntradaCano,2));
    }
    //pressao = volume/bitola^2

    public void setVolume(double volume) {
        this.volume = clamp(volume, MIN_VOLUMEM3, MAX_VOLUMEM3);
        atualizarPressao();
    }

    public void setTamEntradaCano(double tamEntradaCano) {
        this.tamEntradaCano = clamp(tamEntradaCano, MIN_BITOLA_MM, MAX_BITOLA_MM);
        atualizarPressao();
    }

    public double getVolume() {
        return volume;
    }
    public double getTamEntradaCano() {
        return tamEntradaCano;
    }
    public double getPressao() {
        return pressao;
    }

    private void atualizarPressao() {
        double p = (tamEntradaCano > 0) ? volume / (pow(tamEntradaCano,2)) : 0.0;
        this.pressao = clamp(p, 0.0, 16.0); //máximo de 16 bars
    }

}
