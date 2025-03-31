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
                         userRemoteConfigs: [[url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git',
                         credentialsId: 'ssh_key']]])
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    bat 'npm ci --no-audit'  // Ignora vulnerabilidades durante o build
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat 'npx next build'
                }
            }
        }

        stage('Start Application') {
            steps {
                script {
                    bat """
                        @echo off
                        set PORT=${APP_PORT}
                        echo "Iniciando aplicação Next.js na porta %PORT%"
                        npx next start --port %PORT%
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
            echo 'Build falhou - verifique os logs completos'
            script {
                // Comando mais robusto para verificar e matar processos Node
                bat '''
                    @echo off
                    tasklist /FI "IMAGENAME eq node.exe" | find /I "node.exe" > nul
                    if %errorlevel% equ 0 (
                        taskkill /F /IM node.exe /T
                        echo Processos Node.js finalizados
                    ) else (
                        echo Nenhum processo Node.js encontrado
                    )
                '''
            }
        }
    }
}
