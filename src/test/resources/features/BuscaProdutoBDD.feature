# language: pt

Funcionalidade: Teste de buscar o produto

  Cenário: Busca produto com sucesso
    Dado que busco um produto que ja esta cadastrado
    Quando busco esse produto
    Entao recebo uma resposta que o produto foi encontrado com sucesso

  Cenário: Busca produto não cadastrado
    Dado que busco um produto nao cadastrado
    Quando busco esse produto
    Entao recebo uma resposta que o produto nao foi encontrado
