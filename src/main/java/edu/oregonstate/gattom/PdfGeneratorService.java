package edu.oregonstate.gattom;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public class PdfGeneratorService {
    public static void main(String[] args) {
        Javalin app = Javalin.create(JavalinConfig::enableDevLogging).start(7070);
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}
