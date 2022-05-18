package br.com.alura.ecommerce;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/* Class para tratamento de arquivo*/
public class IO {
    /* Método que copia o arquivo source para target*/
    public static void copyTo(Path source, File target) throws IOException {
        /* garantindo a existencia das pastas do targat. Cria as pastas não existentes*/
        target.getParentFile().mkdir();

        /* Copia o arquivo de soure para a pasta de target, e sobrepoe caso arquivo já exista*/
        Files.copy(source, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /* Adicionando conteúdo no arquivo target*/
    public static void append(File target, String conteudo) throws IOException {
        Files.write(target.toPath(), conteudo.getBytes(), StandardOpenOption.APPEND);
    }
}
