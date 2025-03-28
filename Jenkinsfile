pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        // Configuração robusta do PATH para Windows incluindo System32
        PATH = "C:\\Windows\\System32;C:\\Windows;C:\\Windows\\System32\\Wbem;${tool 'nodejs'}\\bin;${env.APPDATA}\\npm;${env.PATH}"
        NODE_ENV = 'production'
        CI = 'true'
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
                    // Verifica explicitamente o cmd.exe primeiro
                    bat '''
                        @echo off
                        echo Verificando ambiente Windows...
                        where cmd
                        where node
                        where npm
                        where npx
                        echo Configurando npm...
                        npm config set cache "${WORKSPACE}\\.npm" --global
                    '''
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Instalação robusta com verificação de erros
                    bat '''
                        @echo off
                        echo Verificando conexão com npm...
                        npm ping
                        echo Atualizando npm...
                        npm install -g npm@latest
                        echo Instalando dependências...
                        npm install
                        echo Verificando instalações...
                        npm list --depth=0
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
            // Adicione notificações adicionais se necessário
        }
    }
}
