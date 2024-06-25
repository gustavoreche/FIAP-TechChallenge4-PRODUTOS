# language: pt

Funcionalidade: Teste de deletar o produto

  Cenário: Deleta produto com sucesso
    Dado que informo um produto que ja esta cadastrado
    Quando deleto esse produto
    Entao recebo uma resposta que o produto foi deletado com sucesso

  Cenário: Deleta produto não cadastrado
    Dado que informo um produto nao cadastrado
    Quando deleto esse produto
    Entao recebo uma resposta que o produto nao foi cadastrado
