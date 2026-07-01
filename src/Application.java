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

    System.out.println("[SYSTEM] Temporizador de intereses configurado 10 minutos. Dentro de 1 minuto se ejecutara el primero.");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("[SYSTEM] Apagando el planificador de tareas...");
        scheduler.shutdown();
    }));
}