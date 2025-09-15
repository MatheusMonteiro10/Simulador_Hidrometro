import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Display extends JFrame {
    private BufferedImage base;
    private BufferedImage atual;
    private int ultimoMarco = -1; // controla atualização

    private final int W = 720;
    private final int H = 480;

    public Display() {
        super("Hidrômetro - Simulador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(W, H);
        setLocationRelativeTo(null);
        carregarOuCriarBase();
        this.atual = deepCopy(base);
        setVisible(true);
    }

    // Atualiza quando acumulado += 1m^3
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
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Fontes para os valores e unidades
            Font valorFont = new Font("Monospaced", Font.BOLD, 36);
            Font unidadeFont = new Font("SansSerif", Font.BOLD, 18);
            g.setFont(valorFont);

            g.setColor(Color.RED);

            // ----- Valor de m³ -----
            String textoM3 = String.format("%.2f", m3);
            FontMetrics fmM3 = g.getFontMetrics();
            int textWidthM3 = fmM3.stringWidth(textoM3);
            int posX_m3 = (getWidth() - textWidthM3) / 2;
            int posY_m3 = 150;

            g.drawString(textoM3, posX_m3, posY_m3);

            // Unidade "m³"
            g.setFont(unidadeFont);
            g.drawString("m³", posX_m3 + textWidthM3 + 10, posY_m3 - 5); // Pequeno ajuste vertical

            // ----- Valor da pressão -----
            g.setFont(valorFont);
            String textoPressao = String.format("%.2f", pressaoBar);
            FontMetrics fmPressao = g.getFontMetrics();
            int textWidthPressao = fmPressao.stringWidth(textoPressao);
            int posX_pressao = (getWidth() - textWidthPressao) / 2;
            int posY_pressao = 250;

            g.drawString(textoPressao, posX_pressao, posY_pressao);

            // Unidade "bars"
            g.setFont(unidadeFont);
            g.drawString("bars", posX_pressao + textWidthPressao + 10, posY_pressao - 5);

        } finally {
            g.dispose();
        }
    }


    private void carregarOuCriarBase() {
        try {
            File f = new File("C:/Users/pc/Downloads/hidrometro3.jpg");  // Caminho para o JPEG enviado
            if (f.exists()) {
                base = ImageIO.read(f);
                if (base.getWidth() != W || base.getHeight() != H) {
                    BufferedImage resized = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = resized.createGraphics();
                    g.drawImage(base, 0, 0, W, H, null);
                    g.dispose();
                    base = resized;
                }
            } else {
                base = criarBaseSintetica(W, H);
            }
        } catch (IOException e) {
            base = criarBaseSintetica(W, H);
        }
    }

    private BufferedImage criarBaseSintetica(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        try {
            GradientPaint gp = new GradientPaint(0, 0, new Color(230, 240, 255), 0, h, new Color(210, 220, 240));
            g.setPaint(gp);
            g.fillRect(0, 0, w, h);
        } finally {
            g.dispose();
        }
        return img;
    }

    private void salvarJPEG() {
        try {
            File arquivo = new File("hidrometro_atualizado.jpg");
            ImageIO.write(atual, "jpg", arquivo);
        } catch (IOException ignored) {}
    }

    private void salvarJPEGComNome(int metroCubico) {
        try {
            String matriculaSUAP = "202111250035";
            String nomePasta = "Medições_" + matriculaSUAP;
            File pasta = new File(nomePasta);
            if (!pasta.exists()) {
                pasta.mkdirs();
            }

            if (metroCubico < 1) return;

            // Mantém valor entre 1 e 99
            int numeroImagem = ((metroCubico - 1) % 99) + 1;

            String nomeArquivo = String.format("%02d.jpeg", numeroImagem);
            File arquivo = new File(pasta, nomeArquivo);
            ImageIO.write(atual, "jpg", arquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (atual != null) {
            g.drawImage(atual, 0, 0, null);
        }
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage copy = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(bi, 0, 0, null);
        g.dispose();
        return copy;
    }
}
