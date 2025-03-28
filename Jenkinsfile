pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        NODE_ENV = 'production'
        CI = 'true'
        npm_config_cache = "${env.WORKSPACE}\\.npm"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Usar cmd.exe explicitamente para garantir que o comando seja executado corretamente
                    bat 'cmd /c npm install'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    bat 'cmd /c npm run build'
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
