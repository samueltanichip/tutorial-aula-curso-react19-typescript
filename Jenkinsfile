pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        PATH = "C:\\Windows\\System32;C:\\Windows;C:\\Windows\\System32\\Wbem;${tool 'nodejs'}\\bin;${env.PATH}"
        APP_PORT = '3000'
        NODE_ENV = 'production'
        CI = 'true'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'LocalBranch', localBranch: 'main']
                    ],
                    userRemoteConfigs: [[
                        url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git',
                        credentialsId: 'github-credentials'
                    ]]
                ])
            }
        }

        stage('Setup Environment') {
            steps {
                script {
                    bat '''
                        @echo off
                        echo System PATH: %PATH%
                        where cmd
                        where node
                        where npm
                    '''
                }
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    bat 'npm install'
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

        stage('Deploy Application') {
            steps {
                script {
                    bat '''
                        @echo off
                        echo Parando processos na porta %APP_PORT%...
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%APP_PORT%') do (
                            taskkill /F /PID %%a
                        )
                        echo Iniciando aplicação...
                        start "ReactApp" /B npm run start
                    '''
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build/**/*', allowEmptyArchive: true
            cleanWs()
        }
    }
}
