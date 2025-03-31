pipeline {
    agent any

    tools {
        nodejs 'nodejs' // Nome exato da instalação do Node no Jenkins
    }

    environment {
        // Garante que o sistema encontrará os comandos básicos
        PATH = "C:\\Windows\\System32;${env.PATH}"
        APP_PORT = 3000
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', 
                         branches: [[name: '*/main']],
                         userRemoteConfigs: [[url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git']]])
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Verifica se os comandos básicos funcionam
                    bat 'where cmd'
                    bat 'where node'
                    bat 'where npm'
                    
                    // Instala dependências com clean install
                    bat 'npm ci'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    // Executa o build do Next.js com produção
                    bat 'npx next build'
                    
                    // Opcional: gera análise do bundle
                    bat 'npx next analyze'
                }
            }
        }

        stage('Start Application') {
            steps {
                script {
                    // Mata processos existentes na porta definida
                    bat """
                        @echo off
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :${APP_PORT}') do (
                            taskkill /f /pid %%a
                        )
                        start "NextJS_Server" cmd /c "npm run start"
                    """
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline concluído'
            archiveArtifacts artifacts: '.next/**/*', allowEmptyArchive: true
        }
        failure {
            echo 'Build falhou - verifique os logs'
            // Limpeza de processos em caso de falha
            bat 'taskkill /F /IM node.exe /T'
        }
    }
}
