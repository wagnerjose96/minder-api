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
Passo 5 - Para que sua branch seja enviada para o repositório, execute o comando: git push origin nomeDaBranch
Passo 6 - Para baixar o código que esta na dev para sua branch, de o comando: git pull
Passo 7 - Comece a trabalhar no seu código, e quando der commit, realize o mesmo na sua branch.

Como subir o código na sua branch:

Passo 1 - Verifique em que branch você está;
Passo 2 - Entre na sua branch caso não esteja nela, utilizando o comando: git checkout nomeDaSuaBranch
Passo 3 - De o comando git pull para baixar as atualizações dos outros membros do time (Caso de conflito, resolver o conflito na sua branch e depois subir o código)
Passo 4 - Depois de resolver o conflito, se houver, realize a sequência de comando padrão para subir o código: 
            -> git add * 
            -> git commit -m "comentário"   
            -> git push origin nomeDaSuaBranch

Como dar merge request: 

Passo 1 - Vá até a branch que você quer dar o Merge request (normalmente a DEV);
Passo 2 - Execute o comando: git merge nomeDaSuaBranch (Ao fazer isso, você irá unir o código da sua branch com o da DEV);
Passo 3 - Execute os comandos padrões para subir o repositório atualizado no gitlab.
