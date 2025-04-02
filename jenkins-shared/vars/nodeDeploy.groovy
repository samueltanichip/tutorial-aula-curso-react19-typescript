
def call(String environment = 'staging') {
    def dockerTemplate = libraryResource('templates/Dockerfile.node.template')
    def packageJson = readJSON file: 'package.json'

    try {
        // Gera Dockerfile dinâmico
        writeFile file: 'Dockerfile', text: dockerTemplate

        // Build da imagem Docker
        docker.build("myapp:${packageJson.version}-${environment}")

        // Push para registry (exemplo para AWS ECR)
        docker.withRegistry('https://123456789.dkr.ecr.us-east-1.amazonaws.com', 'ecr-credentials') {
            docker.image("myapp:${packageJson.version}-${environment}").push()
        }

        // Deploy em Kubernetes (exemplo)
        if (environment == 'production') {
            sh """
                kubectl set image deployment/myapp \
                myapp=123456789.dkr.ecr.us-east-1.amazonaws.com/myapp:${packageJson.version}-${environment}
            """
        }

    } catch (Exception e) {
        echo "🚨 ERRO no deploy: ${e.message}"
        slackSend channel: '#deploys',
                  color: 'danger',
                  message: "Falha no deploy ${environment} - ${env.BUILD_URL}"
        throw e
    }
}
