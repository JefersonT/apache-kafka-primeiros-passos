package br.com.alura.ecommerce;

import br.com.alura.ecommerce.dispatcher.KafkaDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();/* Criando um KafkaDispatcher para cria um Producer*/

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*Criando email aleatório temporarioamente*/
            var email = req.getParameter("email");

            /* Declarando um orderID recebendo via http get*/
            var orderId = req.getParameter("uuid");

            /* Declarando o amount, o valor da orden em BigDecimal*/
            var amount = BigDecimal.valueOf(Long.parseLong(req.getParameter("amount"))); /* Valor entre 1 e 5000*/

            /* Criando uma nova Order*/
            var order = new Order(orderId, amount, email);

            var database =  new OrdersDatabase();
            if (database.saveNew(order)){
                /* Enviando a orden com o userID para o topic ECOMMERCE_NEW_ORDER*/
                orderDispatcher.send("ECOMMERCE_NEW_ORDER",
                        email,
                        order,
                        new CorrelationId(NewOrderServlet.class.getSimpleName())); // CorrelationId inicial,
                // como este é o primeiro a disparar uma mensagem ele receber um novo id para ser passado para os proximos

                System.out.println("New order sent successfully!");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("New order sent!");
            } else {
                System.out.println("Old order received!");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("Old order received!");
            }



        } catch (SQLException | ExecutionException | InterruptedException e) {
            throw new ServletException(e);
        }
    }
}