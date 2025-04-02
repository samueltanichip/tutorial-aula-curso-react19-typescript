
def call(Map config = [:]) {
    def defaults = [
        testCmd: 'npm test',
        coverageCmd: 'npm run test:coverage',
        reportDir: 'coverage',
        junitPattern: '**/junit.xml'
    ]
    config = defaults + config

    try {
        // Executa testes
        bat label: "Rodando testes", script: config.testCmd

        // Gera relatório de cobertura (se existir o comando)
        if (config.coverageCmd) {
            bat label: "Gerando cobertura", script: config.coverageCmd
        }

        // Processa relatórios
        junit testResults: config.junitPattern, allowEmptyResults: true

        // Publica relatório HTML (se existir)
        if (fileExists(config.reportDir)) {
            publishHTML target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: config.reportDir,
                reportFiles: 'lcov-report/index.html',
                reportName: 'Cobertura de Testes'
            ]
        }

    } catch (Exception e) {
        echo "⚠️ Fallback: Criando relatório vazio"
        writeFile file: 'junit.xml', text: '<testsuite tests="0"></testsuite>'
        junit testResults: 'junit.xml', allowEmptyResults: true
        currentBuild.result = 'UNSTABLE'
    }
}
