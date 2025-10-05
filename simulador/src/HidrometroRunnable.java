public class HidrometroRunnable implements Runnable {

    private final int id;
    private final String matricula;
    private final Input input;
    private final Medidor medidor;
    private final Display display;

    private volatile boolean running = true;
    private volatile boolean paused = false;

    public HidrometroRunnable(int id, String matricula) {
        this.id = id;
        this.matricula = matricula;

        // parâmetros iniciais
        double inicialBitolaMm = 30.0;
        double inicialVolumeCm3 = 10.0;

        // cada simulador tem seus próprios componentes
        this.input = new Input(inicialBitolaMm, inicialVolumeCm3);
        this.medidor = new Medidor();
        this.display = new Display(id, matricula);

        // registra início da instância em LogWindow
        LogWindow.getInstance().log(String.format("[SIM %d] Iniciado.", id));
    }

    @Override
    public void run() {
        long iter = 0;

        try {
            while (running) {
                synchronized (this) {
                    while (paused && running) {
                        wait();
                    }
                }

                // calcula a vazão em m^3
                double vazao = input.getVolumeComAr() / 1_000.0;

                // atualiza o medidor com a vazão calculada
                medidor.medir(vazao);

                // exibe no display
                display.apresentar(medidor.getAcumuladoM3(), input.getPressao());

                // a cada 10 iterações imprime na janela de log o estado atual da instância do simulador
                if (iter % 10 == 0) {
                    LogWindow.getInstance().log(
                            String.format("[SIM %d] Ciclo %d -> Vol=%.2f cm³ | Bitola=%.2f mm | Pressao=%.2f | Acum=%.2f m³",
                                    id, iter + 1,
                                    input.getVolume(),
                                    input.getTamEntradaCano(),
                                    input.getPressao(),
                                    medidor.getAcumuladoM3()
                            )
                    );
                }

                // incrementa o ciclo
                iter++;

                // ritmo da simulação
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LogWindow.getInstance().log(String.format("[SIM %d] Interrompido.", id));
        } finally {
            LogWindow.getInstance().log(String.format("[SIM %d] Finalizado. Acumulado=%.3f m³", id, medidor.getAcumuladoM3()));
        }
    }

    public synchronized void setVolume(double v) {
        input.setVolume(v);
        LogWindow.getInstance().log(String.format("[SIM %d] Volume ajustado para %.2f cm³", id, v));
        System.out.printf("[SIM %d] Volume ajustado para %.2f cm³%n", id, v);
    }

    public synchronized void setBitola(double b) {
        input.setTamEntradaCano(b);
        LogWindow.getInstance().log(String.format("[SIM %d] Bitola ajustada para %.2f mm", id, b));
        System.out.printf("[SIM %d] Bitola ajustada para %.2f mm%n", id, b);
    }

    public synchronized void pause() {
        paused = true;
        LogWindow.getInstance().log(String.format("[SIM %d] Pausado.", id));
        System.out.printf("[SIM %d] Pausado.", id);
    }

    public synchronized void resume() {
        if (paused) {
            paused = false;
            notify();
            LogWindow.getInstance().log(String.format("[SIM %d] Retomado.", id));
            System.out.printf("[SIM %d] Retomado.", id);
        }
    }

    public synchronized void stop() {
        running = false;
        // acorda caso esteja pausado
        resume();
    }

    public int getId() { return id; }
}
