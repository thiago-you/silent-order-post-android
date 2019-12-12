# Silent Order Post SDK Android

## Introdução

O SDK **Silent Order Post** é uma integração que a **Cielo** oferece aos lojistas, onde os dados de pagamentos são trafegados de forma segura, mantendo o controle total sobre a experiência de checkout.

Esse método possibilita o envio dos dados do pagamento do seu cliente de forma segura, diretamente em nosso sistema. Os dados de pagamento, tais como número do cartão e data de validade são armazenados no ambiente da **Cielo**, que conta com a certificação PCI DSS 3.2.

## Características

- Captura de dados de pagamento diretamente para os sistemas da Cielo por meio dos campos definidos no seu checkout através dop SDK.
- Compatibilidade com todos os meios de pagamentos disponibilizados no Gateway Pagador (Nacional e Internacional)
- Redução do escopo de PCI DSS
- Mantenha controle total sobre a experiência de checkout mantendo o look & feel de sua marca


## Integração

### 1. Obtendo o AccessToken

Para usar o SDK, é necessário que o estabelecimento gere o AccessToken a partir da API de autenticação da Cielo (oAuth).

Isso é feito através de uma solicitação **POST** para o seguinte *endpoint*:

```https://transactionsandbox.pagador.com.br/post/api/public/v1/accesstoken?merchantid={mid}```

No lugar do ```{mid}``` deve-se preencher o **MerchantID** de sua loja na plataforma *Pagador da Cielo*.

Exemplo:

 ```https://transactionsandbox.pagador.com.br/post/api/public/v1/accesstoken?merchantid=00000000-0000-0000-0000-000000000000```

#### Requisição

    POST ```/v1/accesstoken?merchantid={mid}```

| Propriedade |	Descrição |	Tipo | Tamanho | Obrigatório |
|-------------|-----------|------|---------|------------|
| mid |	Identificador da loja no Pagador |	Guid |	36 	| Sim |


#### Resposta
 
Como resposta, o estabelecimento receberá um json (HTTP 201 Created) contendo entre outras informações o ticket (AccessToken)

 
| Propriedade |	Descrição |	Tipo | Tamanho |	Formato |
|-------------|-----------|------|---------|------------|
| MerchantId |	Identificador da loja no Pagador |	Guid |	36 	|xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx |
| AccessToken | 	Token de Acesso. Por questões de segurança, este ticket dará permissão para o estabelecimento salvar apenas 1 cartão dentro de um prazo de já estipulado na resposta, através do atributo ExpiresIn (por padrão, 20 minutos). O que acontecer primeiro invalidará esse mesmo ticket para um uso futuro. | Texto | – | 	NjBhMjY1ODktNDk3YS00NGJkLWI5YTQtYmNmNTYxYzhlNjdiLTQwMzgxMjAzMQ== |  
| Issued | Data e hora da geração | Texto | –  | AAAA-MM-DDTHH:MM:SS | 
| ExpiresIn | Data e hora da expiração	| Texto | - |AAAA-MM-DDTHH:MM:SS |


    Por questões de segurança, será requerido obrigatoriamente o cadastro de um IP válido do estabelecimento na Cielo. Caso contrário a requisição não será autorizada (HTTP 401 NotAuthorized). Por favor, identificar qual será o IP de saída que acessará a API e na sequência solicitar o cadastro do mesmo através do canal de atendimento Cielo