pipeline {
    agent any

    tools {
        nodejs 'nodejs' // Nome da instalação do Node.js no Jenkins
    }

    environment {
        // Variáveis de ambiente úteis
        APP_PORT = '3000'
        NODE_ENV = 'production'
        PATH = "${env.PATH};${tool 'nodejs'}/bin" // Garante que npm/node estão no PATH
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    extensions: [[$class: 'CleanBeforeCheckout']], // Limpa o workspace antes
                    userRemoteConfigs: [[
                        url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git',
                        credentialsId: '' // Adicione seu ID de credencial se o repositório for privado
                    ]]
                ])
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Verifica versões (para debug)
                    bat 'node --version'
                    bat 'npm --version'

                    // Instala dependências com cache (mais rápido em builds futuros)
                    bat 'npm ci --prefer-offline' // Usa package-lock.json para instalação exata
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    // Build da aplicação React/TypeScript
                    bat 'npm run build' // Ou o comando de build do seu projeto (ex: 'npm run build:prod')

                    // Opcional: Testes unitários
                    bat 'npm test'
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    // 1. Mata processos existentes na porta 3000
                    bat '''
                        @echo off
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%APP_PORT%') do (
                            taskkill /F /PID %%a
                        )
                    '''

                    // 2. Inicia o servidor em background (Windows)
                    bat 'start /B npm run start' // Ou 'node server.js' se for um servidor Node.js

                    // 3. Verifica se a aplicação está rodando
                    bat 'timeout /t 10 /nobreak > nul' // Aguarda 10 segundos
                    bat "curl -I http://localhost:%APP_PORT% || echo \"Aplicação não respondeu\""
                }
            }
        }
    }

    post {
        success {
            echo 'Build e deploy concluídos com sucesso!'
            // Opcional: Notificação (Slack, Email, etc.)
        }
        failure {
            echo 'Pipeline falhou. Verifique os logs.'
            bat 'taskkill /F /IM node.exe /T 2> nul || echo "Nenhum processo Node encontrado"'
        }
        always {
            // Limpeza opcional
            archiveArtifacts artifacts: 'build/**/*', onlyIfSuccessful: true // Salva o build
            junit '**/test-results.xml' // Se usar testes JUnit
        }
    }
}
