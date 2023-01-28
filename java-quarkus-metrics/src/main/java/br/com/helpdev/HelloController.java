package br.com.helpdev;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ThreadLocalRandom;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

@Path("/")
public class HelloController {

    private static final int LENGTH_LIMIT = 10;

    @Inject
    MeterRegistry registry;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    // Exemplo de instrumentação de tempo por annotation
    @Timed("hello_get_method_timer")
    public String getString(@QueryParam("name") final String name) {

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