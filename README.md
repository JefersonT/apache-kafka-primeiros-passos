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
    - Executar a classe *FraudDetectorService* no módulo **service-fraud-detector**.
    - Executar a classe *SendEmailService* no módulo **service-send-email**.
    - Executar a classe *LogService* no módulo **service-log**.
    - Executar a classe *CreateUserService* no módulo **service-users**.
    - Executar a classe *NewOrderMain* no módulo **service-new-order**.
-  Ao executar o NewOrderMain é possível notar nas demais janelas de execução os consumers recebendo a order disparada pelo producers.
## Anotações
- Praticando diretamento no Kafka:
    - Com após executar o comando ```$ docker-compose up -d```, execute o comando ```docker ps```, para identificar o nome do container que esta rodando o kafka;
    - Execute o seguinte comando para entrar o container:
    ```
    $ docker exec -it kafka_nome_container bash
    ```
    - Neste container o kafka e o zookeeper já são configurados e iniciados. Caso não utilize este método será necessário baixar o binário do [Kafka](https://kafka.apache.org/downloads) e executar o seguintes passos:
        - Extrair o arquivo baixado.
        - Acessar a pasta extraida via terminal.
        - Executar o comando:
        ```
        $ bin/zookeeper-server-start.sh config/zookeeper.properties
        ```
        - Em una nova janela do terminal executar o comando:
        ```
        $ bin/kafka-server-start.sh config/server.properties
        ```
        - Por padrão o kafka executa no localhost:9092 e o zookeeper em localhost:2181.
    - Comando para criar um topics.(caso não esteja usando o docker será necessário adicionar o *bin/* antes do comando .sh):
    ```
    $ kafka-topics.sh --create --boostrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic NOME_TOPICO
    ```
    - Listar os topicos:
    ```
    $ kafka-topics.sh --list --bootstrap-server localhost:9092
    ```
    - Enviar mensagem para o tópico
        ```
        $ kafka-console-producer.sh --broker-list localhost:9092 --topc NOME_TOPICO
        ```
        - Cada linha enviada será uma mensagem enviada para o tópico.
    - Criando consumer que irá receber as mensagens enviadas:
        - Em uma nova aba do terminal digite:
        ```
        $ kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_TOPICO
        ```
        - Para ver as mensagens chegando deve enviar pelo produtor criado anteriormente.
    - Criando um consumer que irá receber as mensagens que já foram enviadas e as proximas:
        ```
        $ kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic NOME_TOPICO --from-beginning
        ```
    


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
    