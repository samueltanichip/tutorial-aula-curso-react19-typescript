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
                    bat 'yarn install --frozen-lockfile'  // Usando Yarn para instalar as dependências
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat 'yarn build'  // Usando Yarn para build
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
                        yarn start --port %PORT%  // Usando Yarn para iniciar a aplicação
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
