import javax.swing.*;
import java.awt.*;

public class LogWindow extends JFrame {
    private static LogWindow instance;
    private final JTextArea textArea;

    private LogWindow() {
        setTitle("Painel de Logs SHA");
        setSize(700, 400);
        setLocation(50, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane);

        setVisible(true);
    }

    public static synchronized LogWindow getInstance() {
        if (instance == null) {
            instance = new LogWindow();
        }
        return instance;
    }

    // Metodo thread-safe para registrar mensagens
    public synchronized void log(String mensagem) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(mensagem + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
}