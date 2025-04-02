// Método 1 (Recomendado quando configurado corretamente no Jenkins)
@Library('pipeline_libraryes@jenkins-shared-library') _

// Método 2 (Alternativo direto no código)
// library(
//     identifier: 'tutorial-shared-lib@jenkins-shared-library',
//     retriever: modernSCM([
//         $class: 'GitSCMSource',
//         remote: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git',
//         credentialsId: 'chave ssh'
//     ])
// )

pipeline {
    agent any
    stages {
        stage('Debug') {
            steps {
                script {
                    // Verifique se a library foi carregada
                    echo "Library path: ${library('tutorial-shared-lib').resourcePath}"
                    sh "ls -la ${library('tutorial-shared-lib').resourcePath}/vars/"
                }
            }
        }
        stage('Build') {
            steps {
                nodeBuild()
            }
        }
    }
}
