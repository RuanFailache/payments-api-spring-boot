# Desafio técnico - API de pagamentos

Projeto criado com `Java 17` e `Spring Boot 3` para simular um sistema de pagamentos. Neste foi utilizado conceitos
de `SOLID` e `Clean Architecture` no seu desenvolvimento, além da implementação de testes unitários e de integração
com `Mockito` e `JUNIT`. No banco de dados, foi utilizado `JPA` e `H2`.

## Rodando o projeto

Para rodar o projeto localmente, primeiro faça o clone do projeto localmente em sua máquina com o comando:

```sh
git clone git@github.com:RuanFailache/payments-api-spring-boot.git
```

Após isso, abra o projeto com sua IDE favorita (Intellij, Eclipse, VSCode, etc) e execute o projeto.

Caso prefira executar o projeto pelo terminal, navegue até a raiz do projeto e, no terminal, execute o seguinte comando:

```sh
mvn spring-boot:run
```

## Acessando a documentação

A documentação das rotas foi feita utilizando `Swagger` e para acessa-la no navegador utilize
o [link](http://localhost:8080/docs) caso o projeto esteja rodando localmente em sua máquina.

## Licença

MIT