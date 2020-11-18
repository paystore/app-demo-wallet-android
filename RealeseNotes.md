# Histórico de versões

## App Demo Wallet Smart - Applet Phoebus

### Versão: 1.1.0.0
- 62146
  - Remover auto-preenchimento dos campos nas telas de cadastro do portador,
revalidacao do portador e cadastro do cartao do portador
- 62110
  - Enviar null quando usuario nao preencher campo de cvv
- 61225
  - Exibir texto completo da notificação
- 59128
  - Renomear validateCardHolder -> revalidateCardHolder
  - Renomear activitys -> activities
- 61245
  - Checar se há mensagem de erro antes de exibir o erro.
  - Remover logs de erro duplicados.
- 61224
  - Ajuste nos campos da listagem de pagamento
- 61155
  - Criar canal de notificação e configurar a exibição.
-  55894
  - Ajuste para exibir toast quando lista de pagamento vier vazia.
  - Ajustando de merchantPaymentId para appTransactionId
  - Ajustando a listagem de acordo com o model da lib.
- 61148
  - Configurar conta do Firebase da phoebus
  - Melhorar logs e atualizar versão
  - Remover e ignorar arquivos gerados pela ide (.idea/)
- 60977
  - Atualizar leitura e exibição das informações do QRCode
-55884
  - Usando ultimo cardId e cardHolderId na tela de pagamentos para facilitar o uso.
  - Corrigir contexto passado na funcao de exibir o dialog nas telas de realizar e consulta pagamento
  - Melhorar logs da requisição de pagamento
  - Removendo o envio de campos que nao estao mais sendo usados para realizacao do pagamento.
  - Adicionando funcao  de pegar o ip.
  - Adicao dos novos campos no layout de consulta.
  - Ajustando o nome do model da requisicao de pagamentos.
  - Adicionando atividade que exibira a listagem dos pagamentos no android manifest.
  - Concatenando o Bearer com o token para autenticacao.
  - Criacao do recycler view que vai fazer a listagem dos pagamentos

### Versão: 1.0.3.0
- 58479
  - atualizando a url para obter o token da api
  - removendo constante não utilizada
  - Correção do nome Credencial para Credential
  -  Adição de novos campos na página de configuração.
  - Realizando requisição para obter token.

### Versão: 1.0.2.0
- 58317
  - Caso não consiga pegar o IMEI do aparelho será capturado o ID do Software do aparelho

### Versão: 1.0.1.0
-57913
  - Separacao do app demo wallet da lib wallet

### Versão: 1.0.0.0
**Versão Inicial**
