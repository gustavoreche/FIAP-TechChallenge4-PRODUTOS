# language: pt

Funcionalidade: Teste de buscar o produto

  Cenário: Tem estoque do produto com sobra
    Dado que busco um produto que tem estoque sobrando
    Quando verifico se tem estoque desse produto
    Entao recebo uma resposta que o produto tem estoque

  Cenário: Tem estoque do produto com a quantidade exata
    Dado que busco um produto que tem estoque na quantidade exata
    Quando verifico se tem estoque desse produto
    Entao recebo uma resposta que o produto tem estoque

  Cenário: Não tem estoque do produto
    Dado que busco um produto que não tem estoque
    Quando verifico se tem estoque desse produto
    Entao recebo uma resposta que o produto não tem estoque

  Cenário: Busca produto não cadastrado
    Dado que verifico um produto nao cadastrado
    Quando verifico se tem estoque desse produto
    Entao recebo uma resposta que o produto nao existe
