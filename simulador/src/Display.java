import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Display extends JFrame {
    private BufferedImage base;
    private BufferedImage atual;
    private int ultimoMarco3m3 = -1; // controla atualização a cada 3 m^3

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

    // Atualiza somente quando passar de múltiplos de 3 m^3
    public void apresentar(double m3, double pressaoBar) {
        int marco = (int) Math.floor(m3 / 3.0);
        if (marco != ultimoMarco3m3) {
            ultimoMarco3m3 = marco;
            desenharValores(m3, pressaoBar);
            repaint();
            salvarJPEG();
        }
    }

    private void desenharValores(double m3, double pressaoBar) {
        this.atual = deepCopy(base);
        Graphics2D g = atual.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


// Áreas de mostradores (numéricos) — simples retângulos
            int panelWidth = W - 80;
            int panelHeight = 80;


// Mostrador de m^3
            int x1 = 40;
            int y1 = 100;
            desenharMostrador(g, x1, y1, panelWidth, panelHeight, "Metros cúbicos (m³)", String.format("%.2f", m3));


// Mostrador de pressão
            int x2 = 40;
            int y2 = 220;
            desenharMostrador(g, x2, y2, panelWidth, panelHeight, "Pressão (bar)", String.format("%.2f", pressaoBar));


// Rodapé
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g.setColor(new Color(0,0,0,180));
            g.drawString("Plotado ao atingir múltiplos de 3 m³", 40, H - 40);
        } finally {
            g.dispose();
        }
    }

    private void desenharMostrador(Graphics2D g, int x, int y, int w, int h, String titulo, String valor) {
// Fundo do mostrador
        g.setColor(new Color(245, 245, 245));
        g.fillRoundRect(x, y, w, h, 30, 30);
        g.setColor(new Color(60, 60, 60));
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(x, y, w, h, 30, 30);


// Título
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString(titulo, x + 20, y + 30);


// Valor grande ao centro
        g.setFont(new Font("Monospaced", Font.BOLD, 38));
        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(valor);
        int textX = x + (w - textW) / 2;
        int textY = y + (h + fm.getAscent()) / 2 - 8;
        g.drawString(valor, textX, textY);
    }

    private void carregarOuCriarBase() {
        try {
            File f = new File("hidrometro_base.jpg");
            if (f.exists()) {
                base = ImageIO.read(f);
// Redimensiona para W x H se necessário
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
// Fundo gradiente simples
            GradientPaint gp = new GradientPaint(0, 0, new Color(230, 240, 255), 0, h, new Color(210, 220, 240));
            g.setPaint(gp);
            g.fillRect(0, 0, w, h);


// Cabeçalho
            g.setColor(new Color(40, 70, 120));
            g.fillRect(0, 0, w, 64);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 26));
            g.drawString("HIDRÔMETRO", 24, 40);
        } finally {
            g.dispose();
        }
        return img;
    }

    private void salvarJPEG() {
        try {
            // Sempre sobrescreve o mesmo arquivo
            File arquivo = new File("hidrometro.jpg");
            ImageIO.write(atual, "jpg", arquivo);
        } catch (IOException ignored) {}
    }

    @Override
    public void paint(Graphics g) {
// Usar paint para desenhar a imagem atual
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