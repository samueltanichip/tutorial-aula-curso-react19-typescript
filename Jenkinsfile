pipeline {
    agent any

    tools {
        nodejs 'nodejs'  // Define a ferramenta Node.js a ser usada
    }

    environment {
        NODE_ENV = 'production'
        CI = 'true'
        npm_config_cache = "${env.WORKSPACE}\\.npm"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm  // Usando o SCM padrão do Jenkins para checkout
            }
        }

        stage('Install Dependencies') {
            steps {
                script {
                    // Instalar dependências do projeto
                    bat 'npm install'
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    // Rodar o comando de build do Next.js
                    bat 'npm run build'
                }
            }
        }
    }

    post {
        always {
            // Arquivar artefatos da build
            archiveArtifacts artifacts: '**/.next/**/*', allowEmptyArchive: true
            cleanWs()  // Limpar workspace
        }
        failure {
            echo 'Build falhou. Verifique os logs completos.'
        }
    }
}
