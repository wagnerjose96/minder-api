## Passos para gerar corretamente a documentação do seu código.

## 1. Na classe Escoladeti2018Application é configurado o Swagger. No método public SwaggerSpringMvcPlugin groupOnePlugin() na linha 31 é necessário colocar o nome dos seus pacotes para que a documentação contemple eles.

* linha: 
.includePatterns("/alergias.*?") 

* Para adicionar os pacotes é necessário escrever nos paramêtros "/ <nomedopacote depois do br.hela.>.*?"
Exemplo: "/doencas.*?"

## 2. Na classe Controller é onde deverá ser adcionado as anotações que o Swagger irá mapear para realizar a documentação.

## 2.1 Antes da anotação @RestContoller é necessário acdicionar a anotação:
    * @Api(value = "alergia", description = "Documentação dos métodos do AlergiaController")
        value é o nome do seu pacote. Ex: alergia, cirurgia, usuário...
        description é a descrição que é definida para a sua documentação

    * Segundo a anotação descrita no documento ficaria assim:
        alergia: Documentação dos métodos do AlergiaController

## 2.2 Antes de cada método da API é necessário adicionar anotações para o Swagger mapear se deve ou não documentá-lo:
     
    * Para o Swagger documentá-lo é necessário:
        @ApiOperation(value = "Busque todas as alergias" ): Define que esse método será documentado com a descrição determinada pelo value.
  
    * Para não documentar um método é necessário a anotação:
        @ApiIgnore: 
        OBS: métodos privados o Swagger por padrão não documenta.

* PARA VER A DOCUMENTAÇÃO: http://localhost:9090/sdoc.jsp 


   


    



