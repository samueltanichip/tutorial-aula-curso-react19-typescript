// Método 1 (Recomendado quando configurado corretamente no Jenkins)
@Library('jenkins-shared@jenkins-shared-library')_

pipeline {
    agent any
    stages {
        stage('Debug') {
            steps {
                script {
                    // Verifique se a library foi carregada
                    echo "Library path: ${library('pipeline_libraryes').resourcePath}"
                    sh "ls -la ${library('pipeline_libraryes').resourcePath}/vars/"
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    nodeBuild()
                }
            }
        }
    }
}
