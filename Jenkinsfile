@Library('jenkins-shared-library@main') _

pipeline {
    agent any // Agora pode rodar em qualquer agente disponível
    
    environment {
        PATH = "C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;${env.PATH}"
    }

    stages {
        stage('Setup') {
            steps {
                bat '''
                    echo %PATH%
                    where cmd
                    node --version
                    npm --version
                '''
            }
        }
        
        stage('Build') {
            steps {
                bat 'npm install'
                bat 'npm run build'
            }
        }
        
        stage('Test') {
            steps {
                bat 'npm test'
            }
        }
    }
}

