pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        PATH = "C:\\Windows\\System32;${env.PATH};C:\\Program Files\\nodejs\\;${env.APPDATA}\\npm"
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

        stage('Setup Yarn') {
            steps {
                script {
                    // Instala o Yarn globalmente se não estiver disponível
                    bat '''
                        @echo off
                        yarn --version > nul 2>&1
                        if %errorlevel% neq 0 (
                            echo Instalando Yarn globalmente...
                            npm install -g yarn
                        )
                        yarn --version
                    '''
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    bat 'yarn install --frozen-lockfile'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat 'yarn build'
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
                        yarn start --port %PORT%
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
