package br.com.alura.ecommerce;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Authentication;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/* Serviço para disparar ordem de Gerar relatório*/
public class GenerateAllReportsServlet extends HttpServlet {

    private final KafkaDispatcher<String> batchDispatcher = new KafkaDispatcher<>();/* Criando um KafkaDispatcher para cria um Producer*/

    /* Definindo método para fechar o dispatcher*/
    @Override
    public void destroy() {
        super.destroy();
        batchDispatcher.close();
    }

    /* método que executa o dispara da ordem de generete e envio de confirmação*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /* Enviando a ordem de generete para todos os usuários*/
            batchDispatcher.send("SEND_MESSAGE_TO_ALL_USERS",
                    "USER_GENERATE_READING_REPORT",
                    "USER_GENERATE_READING_REPORT");

            /* Cconfirmação de envio*/
            System.out.println("sent generate reporto to all users");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Reprot requests generated");

        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }
}