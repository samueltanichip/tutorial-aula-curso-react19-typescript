pipeline {
    agent any

    tools {
        nodejs 'nodejs' // Nome exato da instalação do Node no Jenkins
    }

    environment {
        // Garante que o sistema encontrará os comandos básicos
        PATH = "C:\\Windows\\System32;${env.PATH}"
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
                    
                    // Instala dependências
                    bat 'npm install'
                }
            }
        }

        stage('Start Application') {
    steps {
        script {
            // Mata processos existentes e inicia em background
            bat '''
                @echo off
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000') do (
                    taskkill /f /pid %%a
                )
                start "NodeServer" cmd /c "node server.js"
            '''
        }
    }
}
    }

    post {
        always {
            echo 'Pipeline concluído'
            // Opcional: matar processos node se necessário
            // bat 'taskkill /F /IM node.exe /T'
        }
    }
}
