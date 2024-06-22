# language: pt

Funcionalidade: Teste de atualização de produto

  Cenário: Atualiza produto com sucesso
    Dado que tenho os dados validos de um produto que ja esta cadastrado
    Quando atualizo esse produto
    Entao recebo uma resposta que o produto foi atualizado com sucesso

  Cenário: Atualiza produto não cadastrado
    Dado que tenho os dados validos de um produto
    Quando atualizo esse produto
    Entao recebo uma resposta que o produto nao esta cadastrado
