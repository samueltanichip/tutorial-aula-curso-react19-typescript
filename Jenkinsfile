@Library('jenkins-shared-library') _

pipeline {
    agent { label 'windows' }  // Executa em agente Windows
    stages {
        stage('Setup') {
            steps {
                bat 'node --version && npm --version'
            }
        }
        stage('Build') {
            steps {
                nodeBuild()
            }
        }
        stage('Test') {
            steps {
                nodeTest()
            }
        }
        stage('Deploy') {
            when { branch 'main' }
            steps {
                nodeDeploy('prod')
            }
        }
    }
}
