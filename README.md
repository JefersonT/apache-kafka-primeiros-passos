# Apache Kafka
Este projeto foi criado para por em prática o aprendizado referente a Kafka. O código esta comentado para melhor compreensão de sua estrutura e funcionamento.

## Pré-requisitos para executar o projeto
- Instalar docker.
- Instalar Java 11.
- Instalar Uma IDE de sua preferencia.
## Executando o projeto
- Baixar o projeto.
- Entrar na raiz do projeto via Terminal.
- Executa o comando para subir os container do kafka:
    ```
    $ docker-compose up -d
    ```
- Abrir a pasta **ecommerce** como projeto Java em sua IDE
    - Executar a classe *FraudDetectorService*
    - Executar a classe *SendEmailService*
    - Executar a classe *LogService*
    - Executar a classe *NewOrderMain*
## Anotações
- Como configurar os registro de forma permante. 
    - Para isso é necessário criar novas pastas tanto para os registros do zookeeper como para o kafka.
    - Acessar o arquivo *config/server.properties* na pasta do kafka e alterar a seguinte linha:
        **DE**:
        ```
        log.dirs=/tmp/kafa-logs
        ```
        **PARA**:
        ```
        log.dirs=caminho/diretorio/criado/anteriomente/kafka
        ```
    - Acessar o arquivo *config/zookeeper.properties* na pasta do kafka e alterar a seguinte linha:
        **DE**:
        ```
        dataDir=/tmp/zookeeper
        ```
        **PARA**:
        ```
        log.dirs=caminho/diretorio/criado/anteriomente/zookeeper
        ```
    - **OBS**.: Neste projeto não há a necessidade de fazer este procedimento, visto que a imagem doker utilizada já possui esta alteração.
    