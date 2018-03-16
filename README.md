--- Boas práticas de utilização do GIT --- 

Esquema de branchs:

-- master (Branch com código de produção)
         -- dev (Branch com código de desenvolvimento)
		-- id#branch1 (Branch com o código relativo a sua tarefa)
		-- id#branch2
		-- id#branch3
		...

Como criar uma nova branch:

Passo 1 - Entrar na pasta do repositório via cmd (linha de comando);
Passo 2 - Digitar o comando git status e verificar em qual branch você está;
Passo 3 - Caso NÃO esteja na branch "dev", executar o comando git checkout dev
Passo 4 - Agora você está na branch dev, para criar uma nova branch, executar o comando: git checkout -b nomeDaBranch 
Passo 5 - 
