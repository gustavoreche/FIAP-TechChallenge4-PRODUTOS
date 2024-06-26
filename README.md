# FIAP-TechChallenge4-PRODUTOS

Quarto desafio da pós graduação da FIAP

Para executar os testes de `BDD` e o teste de integração `ConsumerBaixaNoEstoqueIT.java`, a aplicação tem que estar rodando. Tem um docker-compose no diretório `docker-para-testes` na raiz do projeto, ele provê a app, o banco de dados, e o rabbimq.

Caso queira subir somente o banco de dados, tem um docker-compose no diretório `docker-banco-de-dados`.

- Para rodar os testes com Gatling, execute o seguinte comando: mvn gatling:test