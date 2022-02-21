package edu.oregonstate.gattom;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public class PdfGeneratorService {
    public static void main(String[] args) {
        Javalin app = Javalin.create(JavalinConfig::enableDevLogging).start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));

        /**
         * accept HTML from a POST body.
         */
        app.post("/generate", ctx -> {
            //1. inputs
            String wordListAsHTML = ctx.body();
            System.out.print(wordListAsHTML);
            // or String html = ctx.formParam("html");
            // or bodyAsBytes() or bodyAsInputStream();
        });
    }
}
