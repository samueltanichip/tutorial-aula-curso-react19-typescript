@Library('jenkins-shared-library@main') _

pipeline {
    agent any
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
