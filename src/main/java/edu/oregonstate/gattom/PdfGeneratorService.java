package edu.oregonstate.gattom;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;

public class PdfGeneratorService {
    public static void main(String[] args) {
        Javalin app = Javalin
                .create(JavalinConfig::enableDevLogging)
                .start(getHerokuAssignedPort());

        /* simple test route */
        app.get("/", ctx -> ctx.result("Hello World"));

        /* accept HTML from a POST body */
        app.post("/generate", ctx -> {
            //1. inputs
            String wordListAsHTML = ctx.body();
                // or bodyAsBytes() or bodyAsInputStream();

            InputStream pdfIs = getRenderedPdf(wordListAsHTML);
            ctx.result(pdfIs)
                    .contentType("application/pdf")
                    .header("Content-Disposition", "attachment; filename=wordlist.pdf");

            /*
            import org.xhtmlrenderer.layout.SharedContext;

            try (OutputStream os = new FileOutputStream(outputPdf)) {
                SharedContext sharedContext = renderer.getSharedContext();
                sharedContext.setPrint(true);
                sharedContext.setInteractive(false);
            }*/
        });
    }

    /**
     * TODO not sure I'm comfortable with "static"
     *
     * @param html the content to convert into a PDF
     * @return InputStream the rendered PDF suitable for returning to client by Javalin
     */
    public static InputStream getRenderedPdf(String html) throws IOException {
        //2. Convert to PDF
        Document document = Jsoup.parse(html, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(document.html());
        renderer.layout();

        // switch to using a different output stream,
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);

        // then copy it into an input stream which ctx.response() can accept
        InputStream resultStream = new ByteArrayInputStream(baos.toByteArray());
        baos.close();

        return resultStream;
    }

    /**
     *
     * @return int
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }

        return 7000;
    }
}
