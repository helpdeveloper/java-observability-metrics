package br.com.helpdev.javaspringmetrics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class HelloController {

    private static final int LENGTH_LIMIT = 10;

    private final MeterRegistry registry;

    HelloController(MeterRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/hello")
    @Timed("hello_get_method_timer")
    public String getString(@RequestParam("name") final String name) {

        // Exemplo para registrar uma metrica de duração
        registry.timer("hello_get_do_process_timer").record(() -> {
            doProcess();
        });

        // Exemplo de metrica de contagem; Quando o tamanho do nome for maior que o limite, incrementa
        if (name.length() > LENGTH_LIMIT)
            registry.counter("hello_get_name_length_limit", "length", String.valueOf(name.length()))
                    .increment();

        return "Hello " + name;
    }

    // simula um processamento
    private void doProcess() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
        } catch (InterruptedException e) {
        }
    }
}