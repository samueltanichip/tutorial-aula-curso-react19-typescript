@Library('jenkins-shared-library@main') _

pipeline {
    agent any
    
    environment {
        PATH = "C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\nodejs;${env.PATH}"
        CI = 'true' // Variável de ambiente para modo CI
    }

    stages {
        stage('Setup') {
            steps {
                bat '''
                    echo %PATH%
                    where cmd
                    node --version
                    npm --version
                    npm install -g yarn@latest  // Opcional: garante yarn atualizado
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
            
            // Opcional: Armazena os arquivos de build
            post {
                success {
                    archiveArtifacts artifacts: 'build/**/*', fingerprint: true
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    // Continua mesmo se não encontrar testes
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        bat 'npm test -- --watchAll=false --passWithNoTests'
                    }
                }
            }
            
            // Opcional: Relatório de testes
            post {
                always {
                    junit 'junit.xml' // Se seus testes gerarem este relatório
                }
            }
        }
        
        // Novo estágio opcional para deploy
        stage('Deploy') {
            when {
                branch 'main' // Só executa no branch main
            }
            steps {
                script {
                    echo "Implemente aqui seu deploy (ex: S3, Firebase, etc.)"
                    // bat 'yarn deploy' // Se tiver um script configurado
                }
            }
        }
    }
    
    post {
        always {
            echo "Pipeline concluído - Status: ${currentBuild.result}"
            cleanWs() // Limpa workspace
        }
        failure {
            emailext (
                subject: "ERRO no Pipeline: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: "Verifique o build em ${env.BUILD_URL}",
                to: "seu-email@exemplo.com"
            )
        }
    }
}
