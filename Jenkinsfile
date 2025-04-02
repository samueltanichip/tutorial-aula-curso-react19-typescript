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
        checkout([
          $class: 'GitSCM',
          branches: [[name: '*/main']],
          extensions: [[
            $class: 'CleanBeforeCheckout'
          ]],
          userRemoteConfigs: [[
            credentialsId: 'ssh_key',
            url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git'
          ]]
        ])
      }
    }

    stage('Create Missing Files') {
      steps {
        script {
          if (!fileExists('public/index.html')) {
            if (isUnix()) {
              sh '''
                mkdir -p public
                echo "<!DOCTYPE html><html><head><title>React App</title></head><body><div id='root'></div></body></html>" > public/index.html
              '''
            } else {
              bat '''
                if not exist public mkdir public
                echo ^<!DOCTYPE html^>^<html^>^<head^>^<title^>React App^</title^>^</head^>^<body^>^<div id='root'^>^</div^>^</body^>^</html^> > public\\index.html
              '''
            }
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
          if (isUnix()) {
            sh 'yarn run build'
          } else {
            bat 'yarn run build'
          }
        }
      }
    }
  }

  post {
    always {
      script {
        // Limpeza opcional após o build
        if (isUnix()) {
          sh 'rm -f .env || true'
        } else {
          bat 'del /F .env 2> nul || exit 0'
        }
      }
    }
    success {
      echo "✅ Build successful!"
      archiveArtifacts artifacts: 'build/**/*', fingerprint: true
    }
    failure {
      echo "❌ Build failed! Check logs at: ${env.BUILD_URL}"
    }
  }
}
