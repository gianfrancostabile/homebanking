import cron.InterestTask;
import view.Window;

void main() {
    javax.swing.SwingUtilities.invokeLater(Window::new);

    configureBackgroundTasks();
}

private static void configureBackgroundTasks() {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    InterestTask interestTask = new InterestTask();

    scheduler.scheduleAtFixedRate(
            interestTask,
            1,
            10,
            TimeUnit.MINUTES
    );

    IO.println("[SYSTEM] Temporizador de intereses configurado 10 minutos. Dentro de 1 minuto se ejecutara el primero.");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        IO.println("[SYSTEM] Apagando el planificador de tareas...");
        scheduler.shutdown();
    }));
}