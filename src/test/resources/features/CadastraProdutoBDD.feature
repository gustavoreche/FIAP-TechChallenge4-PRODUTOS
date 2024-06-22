# language: pt

Funcionalidade: Teste de cadastro de produto

  Cenário: Cadastra produto com sucesso
    Dado que tenho dados validos de um produto
    Quando cadastro esse produto
    Entao recebo uma resposta que o produto foi cadastrado com sucesso

  Cenário: Cadastra produto já cadastrado
    Dado que tenho dados validos de um produto que ja esta cadastrado
    Quando cadastro esse produto
    Entao recebo uma resposta que o produto ja esta cadastrado
