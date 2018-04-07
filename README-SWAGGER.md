## Passos para gerar corretamente a documentação do seu código.
	* Obs: a documentação da API só será vista 

## 1. Na classe Controller é onde o Swagger irá mapear os métodos para realizar a documentação.

## 2.1 Antes da anotação @RestContoller é necessário acdicionar a anotação:
    * @Api(" aqui voce pode dar uma decriçao do seu controller")
        
	* Exemplo:
		@Api("Basic Alergia Controller")

    * Segundo a anotação descrita no documento ficaria assim:
        alergia-controller: Basic Alergia Controller

## 2.2 Antes de cada método da API é necessário adicionar anotações para o Swagger mapear como documentá-lo:
    * Para o adicionar uma descrição antes do @"metodo http" (@PostMapping...) coloque:
        @ApiOperation(value = "Busque todas as alergias" ): Define que esse método será documentado com a descrição determinada pelo value.
  
    * Para não documentar um método é necessário a anotação:
        @ApiIgnore: 
        OBS: o Swagger documenta apenas os métodos com a anotação de um método http (@PostMapping...).

* PARA VER A DOCUMENTAÇÃO: http://localhost:9090/swagger-ui.html


   


    



