pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        PATH = "C:\\Windows\\System32;${env.PATH}"
        APP_PORT = 3000
        NODE_ENV = 'production'
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
                    bat 'where cmd'
                    bat 'where node'
                    bat 'where npm'
                    bat 'npm ci'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat 'npx next build'
                    // Removido o next analyze pois não há configuração para ele
                }
            }
        }

        stage('Start Application') {
            steps {
                script {
                    bat """
                        @echo off
                        set PORT=${APP_PORT}
                        echo Iniciando aplicação na porta %PORT%
                        npm run start
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
            // Adicionado tratamento mais robusto para processos node
            bat 'tasklist /FI "IMAGENAME eq node.exe" 2>NUL | find /I "node.exe" >NUL && taskkill /F /IM node.exe /T || echo Nenhum processo node encontrado'
        }
    }
}
