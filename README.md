## Informações gerais do Git
* Informações básicas sobre como utilizar o GitLab

## Configurando suas branchs
*Esquema de branchs
    - master (Branch com código de produção)
      - dev (Branch com código de desenvolvimento)
		    - id#branch1 (Branch com o id relativo a sua tarefa do Redmine)
		    - id#branch2
		    - id#branch3

## Clonando o projeto do GitLab    
* Realizando o git clone no projeto
    - Abra uma nova janela do terminal - "Ctrl + Alt + T"
    - Para realizar o clone do projeto, execute - "git clone http://escoladeti.unicesumar.edu.br:8083/escoladeti2018time03/h-api.git"
    - Caso necessite realizar um "clone" em uma branch - "git pull origin id#branch1"
    
## Importa projeto após o clone
* Importar o projeto no STS
    - Após realizar o clone do projeto importar o projeto pelo passo a passo a baixo.
    - File -> Import -> General -> Projects From Folder or Archive
    - Selecione a pasta do projeto clonado clicando em "Directory"
    - "Finish"

## Criando uma nova branch
* Criar uma nova branch a cada nova task no Redmine
    - Entrar na pasta do repositório pelo terminal - "Ctrl + Alt + T"
    - Digitar o comando "git branch" e verificar em qual branch você está - Referenciada por "*"
    - Caso "NÃO" esteja na branch "dev", executar o comando - "git checkout dev"
    - Agora você está na branch dev, para criar uma nova branch, executar o comando - "git checkout -b Seunome-id#'task'" 
    - Para que sua branch seja enviada para o repositório, execute o comando - "git --set-upstream origin id#'task'"
    - Para baixar o código que esta na dev para sua branch, de o comando - "git pull"
    - Comece a trabalhar no seu código.

## Salvando suas alterações em sua branch
* Realizando o push após "terminar" sua tarefa

    - Verifique em que branch você está - "git branch"
    - Entre na sua branch caso não esteja nela, utilizando o comando - "git checkout Seunome-id#'task'"
    - De o comando "git pull" para baixar as atualizações dos outros membros do time 
    - Caso de conflito, resolver o conflito na sua branch e depois subir o código
    - Depois de resolver o conflito, se houver, realize a sequência de comando padrão para subir o código 
    - 1 - git add * 
    - 2 - git commit -m "comentário"
    - 3 - git push origin id#'task'

## Banco de Dados
* Configuração do PostgreSQL:
    - Após executar o psql, crie o database "create database escoladeti2018;"
    - Logo após, conecte no database "\c escoladeti2018;"
    - Caso necessite sair, execute "\q"
    - Caso necessite alterar a senha do psql, execute "psql \password" e informe sua nova senha.

* Configuração do arquivo application-dev.properties:
    - Procure o arquivo na pasta "src/main/resources"
    - Altere a propriedade "spring.datasource.password" inserindo sua senha do postgreSQL.

## Utilizando a API-REST com o postman
* Após ter configurado o banco psql e executado a API no STS, abra abra o postman:
    - Para executar um comando utilize a uri "http://localhost:9090/exemplo"
    - Funcionalidades já implementadas:
    - Get de todas os exemplos - "GET" - "http://localhost:9090/exemplo" - Não possui atributos
    - Get pelo Id - "GET" - "http://localhost:9090/exemplo/Id" - Na uri, informar o "Id" referente ao objeto a ser retornado
    - Post de novo exemplo - "POST" - "http://localhost:9090/exemplo" - No "Body" selecione "raw" e defina o formato de input como "JSON" e informe o corpo de um novo exemplo
    - Put de um exemplo - "PUT" - "http://localhost:9090/exemplo" - No "Body" selecione "raw" e defina o formato de input como "JSON" e informe o corpo do exemplo a ser alterado, com o id
    - Delete de um exemplo - "DELETE" - "http://localhost:9090/exemplo/id" - Na uri, informar o "Id" referente ao objeto a ser deletado
    
    ## Passos para gerar corretamente a documentação do seu código.
	* Obs: a documentação da API só será vista para os controllers e os métodos http

## 1. Na classe Controller é onde o Swagger irá mapear os métodos para realizar a documentação.

## 2.1 Antes da anotação @RestContoller é necessário adicionar a anotação:
    * @Api(description = "aqui voce pode dar uma decriçao do seu controller")
        
	* Exemplo:
		@Api(description = "Basic Alergia Controller")

    * Segundo a anotação descrita no documento ficaria assim:
        alergia-controller: Basic Alergia Controller

## 2.2 Antes de cada método da API é necessário adicionar anotações para o Swagger mapear como documentá-lo:
    * Para o adicionar uma descrição antes do @"metodo http" (@PostMapping...) coloque:
        @ApiOperation(value = "Busque todas as alergias" ): Define que esse método será documentado com a descrição determinada pelo value.
  
    * Para não documentar um método é necessário a anotação:
        @ApiIgnore: 
        OBS: o Swagger documenta apenas os métodos com a anotação de um método http (@PostMapping...).

* PARA VER A DOCUMENTAÇÃO: http://swagger.minderapplication.com/


   






