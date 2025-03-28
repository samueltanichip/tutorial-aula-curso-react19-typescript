pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        PATH = "C:\\Windows\\System32;C:\\Windows;C:\\Windows\\System32\\Wbem;${tool 'nodejs'}\\bin;${env.PATH}"
        NODE_ENV = 'production'
        CI = 'true'
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
                        credentialsId: 'github-credentials'
                    ]]
                ])
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Instala dependências essenciais primeiro
                    bat 'npm install tailwindcss postcss autoprefixer'
                    // Instala todas as dependências
                    bat 'npm ci --prefer-offline'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    // Gera os arquivos de configuração se não existirem
                    bat 'npx tailwindcss init -p || echo "Tailwind config already exists"'
                    // Executa o build
                    bat 'npm run build'
                }
            }
        }

        stage('Deploy Application') {
            when {
                expression { currentBuild.resultIsBetterOrEqualTo('SUCCESS') }
            }
            steps {
                script {
                    bat '''
                        @echo off
                        echo Iniciando aplicação Next.js...
                        start "NextApp" /B npm run start
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/.next/**/*', allowEmptyArchive: true
            junit '**/junit.xml' 
            cleanWs()
        }
    }
}
