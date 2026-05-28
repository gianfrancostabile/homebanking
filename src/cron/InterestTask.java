package cron;

import model.Product;
import service.ProductService;

import java.util.List;

public class InterestTask implements Runnable {

    private final ProductService productService = ProductService.getInstance();

    @Override
    public void run() {
        System.out.println("[CRON] Iniciando proceso automatizado de generación de intereses...");
        try {
            productService.applyInterestOnAllProducts();
            System.out.println("[CRON] Proceso finalizado con éxito.");
        } catch (Exception e) {
            System.err.println("[CRON ERROR] Ocurrió un fallo crítico al generar los intereses:");
        }
    }
}
