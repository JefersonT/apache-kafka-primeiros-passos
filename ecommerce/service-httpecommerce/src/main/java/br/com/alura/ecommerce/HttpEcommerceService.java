package br.com.alura.ecommerce;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.security.Provider;

/* Criando serviço para serviço http para criar processos via http*/
public class HttpEcommerceService {
    public static void main(String[] args) throws Exception {
        /* criando um serviço para servir na porta 8080*/
        var server = new Server(8080);

        /* declarando um contexto*/
        var context = new ServletContextHandler();

        /* Definindo pasta raiz*/
        context.setContextPath("/");

        /* Definindo um end-point para criação de uma nova ordem*/
        context.addServlet(new ServletHolder(new NewOrderServlet()), "/new");

        /* Definindo end-point para gerar os relatórios dos clientes*/
        context.addServlet(new ServletHolder(new GenerateAllReportsServlet()), "/admin/generate-reports");

        server.setHandler(context);
        server.start();
        server.join();
    }
}
