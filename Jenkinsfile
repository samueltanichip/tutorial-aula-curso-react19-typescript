@Library('jenkins-shared-library@main') _

pipeline {
    agent any
    
    environment {
        PATH = "C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\nodejs;${env.PATH}"
        CI = 'true'
    }

    stages {
        stage('Setup') {
            steps {
                script {
                    sharedLibrary.setupEnvironment()
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    nodeBuild()
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'build/**/*', fingerprint: true
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    nodeTest()
                }
            }
            post {
                always {
                    script {
                        if (fileExists('junit.xml')) {
                            junit 'junit.xml'
                        } else {
                            echo 'Nenhum relatório de testes encontrado (junit.xml)'
                        }
                    }
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
                expression { currentBuild.resultIsBetterOrEqualTo('UNSTABLE') }
            }
            steps {
                script {
                    nodeDeploy()
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline concluído - Status: ${currentBuild.currentResult}"
            cleanWs()
        }
        failure {
            script {
                echo "Build falhou! Consulte: ${env.BUILD_URL}"
            }
        }
    }
}
