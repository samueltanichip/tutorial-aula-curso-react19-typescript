pipeline {
  agent any

  tools {
    nodejs "nodejs"
  }

  environment {
    PATH = "C:\\Windows\\System32;${env.PATH}"
    NODE_OPTIONS = "--max-old-space-size=5120"
    CI = "false"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Verify Structure') {
      steps {
        script {
          if (isUnix()) {
            sh 'ls -la && ls -la public/ || true'
          } else {
            bat 'dir && dir public || echo "Public folder not found"'
          }
        }
      }
    }

    stage('Install dependencies') {
      steps {
        script {
          if (isUnix()) {
            sh 'yarn install --legacy-peer-deps'
          } else {
            bat 'yarn install --legacy-peer-deps'
          }
        }
      }
    }

    stage('Build') {
      steps {
        script {
          // Verifica se é Next.js (pela presença de next.config.js/ts)
          if (fileExists('next.config.js') || fileExists('next.config.ts') {
            if (isUnix()) {
              sh 'yarn run build'
            } else {
              bat 'yarn run build'
            }
          } 
          // Se não for Next.js, assume Create React App
          else {
            if (isUnix()) {
              sh 'yarn run build --openssl-legacy-provider'
            } else {
              bat 'set NODE_OPTIONS=--openssl-legacy-provider && yarn run build'
            }
          }
        }
      }
    }
  }

  post {
    failure {
      echo "❌ Build failed! Check logs at: ${env.BUILD_URL}"
    }
    success {
      echo "✅ Build successful!"
    }
  }
}
