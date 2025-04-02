@Library('tutorial-shared-lib@jenkins-shared-library') _
import org.nodeutils.Versioner

pipeline {
    agent any
    environment {
        NODE_VERSION = Versioner.getNodeEngine()
    }
    stages {
        stage('Build') {
            steps {
                nodeBuild(
                    nodeVersion: env.NODE_VERSION,
                    buildCmd: 'npm run build:prod'
                )
            }
        }
        stage('Test') {
            steps {
                nodeTest(
                    coverageCmd: 'npm run coverage'
                )
            }
        }
        stage('Deploy') {
            when { branch 'main' }
            steps {
                script {
                    echo "Deploying version ${Versioner.getSemVer()}"
                    nodeDeploy('production')
                }
            }
        }
    }
}
