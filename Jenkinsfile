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

        stage('Setup Environment') {
            steps {
                script {
                    // Remove package-lock.json se existir (para evitar conflitos com Yarn)
                    bat '''
                        @echo off
                        if exist package-lock.json (
                            echo Removendo package-lock.json para evitar conflitos com Yarn...
                            del package-lock.json
                        )
                    '''
                    
                    // Instala o Yarn globalmente se não estiver disponível
                    bat '''
                        @echo off
                        yarn --version > nul 2>&1
                        if %errorlevel% neq 0 (
                            echo Instalando Yarn globalmente...
                            npm install -g yarn@1.22.22
                        )
                        yarn --version
                    '''
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Instala dependências com Yarn, ignorando verificações problemáticas
                    bat 'yarn install --frozen-lockfile --ignore-scripts'
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
