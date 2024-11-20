package app.utils;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {

    private Timer timer;
    private TimerTask task;
    private long startTime; // Variable para registrar el tiempo de inicio

    // Método para iniciar el temporizador
    public synchronized void startTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
            }
        };
        startTime = System.currentTimeMillis(); // Registrar el tiempo de inicio
        // Programar la tarea para que se ejecute cada segundo (1000 ms)
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // Método para detener el temporizador y mostrar el tiempo transcurrido
    public synchronized void stopTimer() {
        if (timer != null) {
            task.cancel();   // Cancelar la tarea
            timer.cancel();  // Cancelar el temporizador
            timer.purge();   // Limpiar el temporizador de tareas canceladas

            long endTime = System.currentTimeMillis(); // Registrar el tiempo de finalización
            long elapsedTime = endTime - startTime; // Calcular el tiempo transcurrido

            System.out.println("Temporizador detenido");
            System.out.println("Tiempo transcurrido: " + elapsedTime + " ms");
        }
    }
}
