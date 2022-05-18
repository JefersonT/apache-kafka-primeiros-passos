package br.com.alura.ecommerce;

public class User {
    private final String uuid;

    public User(String uuid) {
        this.uuid = uuid;
    }

    /* Definindo um local e arquivo para os target*/
    public String getReportPath() {
        return "target/" + uuid + "-reporte.txt";
    }

    public String getUuid() {
        return uuid;
    }
}
