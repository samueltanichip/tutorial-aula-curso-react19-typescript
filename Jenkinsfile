pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        // Configuração robusta do PATH para Windows
        PATH = "C:\\Windows\\System32;C:\\Windows;C:\\Windows\\System32\\Wbem;${tool 'nodejs'}\\bin;${env.APPDATA}\\npm;${env.PATH}"
        NODE_ENV = 'production'
        CI = 'true'
        // Configura cache do npm no workspace
        npm_config_cache = "${env.WORKSPACE}\\.npm"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    extensions: [[$class: 'CleanBeforeCheckout']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git',
                        credentialsId: 'ssh_key'
                    ]]
                ])
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    // Verifica e configura o ambiente
                    bat '''
                        @echo off
                        echo Configurando ambiente...
                        where node
                        where npm
                        where npx
                        npm config set cache "${WORKSPACE}\\.npm" --global
                    '''
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Instalação robusta de dependências
                    bat '''
                        @echo off
                        echo Instalando dependências globais...
                        npm install -g npm@latest
                        echo Instalando dependências do projeto...
                        npm install --save-dev tailwindcss postcss autoprefixer
                        npm install
                    '''
                }
            }
        }

        stage('Configure Tailwind') {
            steps {
                script {
                    // Criação segura dos arquivos de configuração
                    bat '''
                        @echo off
                        echo Verificando configurações do Tailwind...
                        if not exist postcss.config.js (
                            echo Criando arquivos de configuração...
                            .\\node_modules\\.bin\\tailwindcss init -p
                        ) else (
                            echo "Arquivos de configuração já existem"
                        )
                    '''
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat 'npm run build'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/.next/**/*', allowEmptyArchive: true
            cleanWs()
        }
        failure {
            echo 'Build falhou. Verifique os logs completos.'
            // Opcional: Enviar notificação por e-mail/Slack
        }
    }
}
