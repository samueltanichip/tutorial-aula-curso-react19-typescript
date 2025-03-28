pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
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
                    bat '''
                        @echo off
                        echo Verificando versões...
                        node -v
                        npm -v
                        
                        echo Limpando cache npm...
                        npm cache clean --force
                        
                        echo Instalando Next.js explicitamente...
                        npm install next@latest react react-dom --save-exact
                        
                        echo Instalando todas as dependências...
                        npm install --no-audit --legacy-peer-deps
                        
                        echo Verificando instalação do Next.js...
                        npx next --version || echo "Next.js não está disponível"
                    '''
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat '''
                        @echo off
                        echo Executando build com npx...
                        npx next build
                        
                        echo Verificando saída do build...
                        if exist .next (
                            echo Build gerado com sucesso
                        ) else (
                            echo Falha no build - pasta .next não encontrada
                            exit 1
                        )
                    '''
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
        }
    }
}
