import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Display extends JFrame {

    private BufferedImage base;
    private BufferedImage atual;
    private int ultimoMarco = -1; // controla a atualizaçao da imagem gerada

    private final int largura = 720;
    private final int altura = 480;
    private final int idSimulador;
    private final String matriculaSUAP;

    public Display(int idSimulador, String matriculaSUAP) {
        super("Hidrômetro - Simulador " + idSimulador);
        this.idSimulador = idSimulador;
        this.matriculaSUAP = matriculaSUAP;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(largura, altura);
        setLocation(100 + (idSimulador - 1) * 60, 100 + (idSimulador - 1) * 60);
        criarBase();
        this.atual = deepCopy(base);
        setVisible(true);
    }

    // Atualiza quando acumulado += 1m³
    public void apresentar(double m3, double pressaoBar) {
        int marco = (int) Math.floor(m3 / 1.0);
        if (marco != ultimoMarco) {
            ultimoMarco = marco;
            desenharValores(m3, pressaoBar);
            repaint();
            salvarJPEG();
            salvarJPEGComNome(marco);
        }
    }

    private void desenharValores(double m3, double pressaoBar) {
        this.atual = deepCopy(base);
        Graphics2D g = atual.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("Monospaced", Font.BOLD, 36));
            g.setColor(Color.RED);

            String textoM3 = String.format("%.2f", m3);
            int posX_m3 = 300;
            int posY_m3 = 150;
            g.drawString(textoM3 + " m³", posX_m3, posY_m3);

            String textoPressao = String.format("%.2f bars", pressaoBar);
            int posY_pressao = 250;
            g.drawString(textoPressao, posX_m3, posY_pressao);
        } finally {
            g.dispose();
        }
    }

    private void criarBase() {
        try {
            File f = new File("C:/Users/pc/Downloads/hidrometro3.jpg"); // caminho para o JPEG
            if (f.exists()) {
                base = ImageIO.read(f);
                if (base.getWidth() != largura || base.getHeight() != altura) {
                    BufferedImage resized = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = resized.createGraphics();
                    g.drawImage(base, 0, 0, largura, altura, null);
                    g.dispose();
                    base = resized;
                }
            } else {
                base = criarBaseSintetica(largura, altura);
            }
        } catch (IOException e) {
            base = criarBaseSintetica(largura, altura);
        }
    }

    private BufferedImage criarBaseSintetica(int largura, int altura) {
        BufferedImage img = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, altura, Color.WHITE));
        g.fillRect(0, 0, largura, altura);
        g.dispose();
        return img;
    }

    private void salvarJPEG() {
        try {
            File arquivo = new File("hidrometro_atualizado_" + idSimulador + ".jpg");
            ImageIO.write(atual, "jpg", arquivo);
        } catch (IOException ignored) {}
    }

    private void salvarJPEGComNome(int metroCubico) {
        try {
            String nomePasta = "Medições_" + matriculaSUAP;
            File pasta = new File(nomePasta);
            if (!pasta.exists()) pasta.mkdirs();

            if (metroCubico < 1) return;

            int numeroImagem = ((metroCubico - 1) % 99) + 1;
            String nomeArquivo = String.format("SIM%d_%02d.jpeg", idSimulador, numeroImagem);
            File arquivo = new File(pasta, nomeArquivo);
            ImageIO.write(atual, "jpg", arquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (atual != null) g.drawImage(atual, 0, 0, null);
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
        return copy;
    }

}
