pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        PATH = "C:\\Windows\\System32;C:\\Windows;C:\\Windows\\System32\\Wbem;${tool 'nodejs'}\\bin;${env.APPDATA}\\npm;${env.PATH}"
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
                        credentialsId: 'ssh_key' // Use o ID correto das suas credenciais
                    ]]
                ])
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    bat 'node --version'
                    bat 'npm --version'
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    bat 'npm install -g npm@latest'
                    bat 'npm install --save-dev tailwindcss postcss autoprefixer'
                    bat 'npm ci --prefer-offline'
                }
            }
        }

        stage('Configure Tailwind') {
            steps {
                script {
                    bat '''
                        if not exist postcss.config.js (
                            npx tailwindcss init -p
                        ) else (
                            echo "Config files already exist"
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

        stage('Deploy') {
            when {
                expression { currentBuild.resultIsBetterOrEqualTo('SUCCESS') }
            }
            steps {
                script {
                    bat '''
                        @echo off
                        echo Parando processos existentes na porta %APP_PORT%...
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :3000') do (
                            taskkill /F /PID %%a
                        )
                        echo Iniciando aplicação...
                        start "NextApp" /B npm run start
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
            echo 'Build falhou. Verifique os logs.'
        }
    }
}
