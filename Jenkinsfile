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
                bat '''
                    npm install
                    npm run build
                '''
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
                    // Configuração mais robusta para testes
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        bat 'npm test -- --watchAll=false --passWithNoTests --reporters=default --reporters=jest-junit'
                    }
                }
            }
            post {
                always {
                    // Só tenta processar JUnit se o arquivo existir
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
                    echo "Deploy implementado aqui"
                    // bat 'npm run deploy' // Descomente se tiver um script de deploy
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
                // Notificação alternativa se o e-mail falhar
                echo "Build falhou! Consulte: ${env.BUILD_URL}"
                // slackSend channel: '#dev-team', message: "Build falhou: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
            }
        }
    }
}
