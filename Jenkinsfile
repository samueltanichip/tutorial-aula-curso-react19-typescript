pipeline {
  agent any

  tools {
    nodejs "nodejs"
  }

  environment {
    PATH = "C:\\Windows\\System32;${env.PATH}"
    NODE_OPTIONS = "--max-old-space-size=5120"
    CI = "false"
    AWS_REGION = 'us-east-1'
    S3_BUCKET = 'jenkinstest-x015r2'
    REACT_APP_CLIENT = 'Ionics'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout([
          $class: 'GitSCM',
          branches: [[name: '*/main']],
          extensions: [],
          userRemoteConfigs: [[
            credentialsId: 'ssh_key',
            url: 'https://github.com/samueltanichip/tutorial-aula-curso-react19-typescript.git'
          ]]
        ])
      }
    }

    stage('Install dependencies') {
      steps {
        echo 'Installing dependencies'
        script {
          if (isUnix()) {
            sh 'yarn install --legacy-peer-deps --ignore-engines'
          } else {
            bat """
              echo "PATH: %PATH%"
              where yarn
              yarn install --legacy-peer-deps --ignore-engines
            """
          }
        }
      }
    }

    stage('Build') {
      steps {
        echo "Building branch ${env.BRANCH_NAME}"
        script {
          if (isUnix()) {
            sh 'yarn run build --openssl-legacy-provider'
          } else {
            bat """
              set NODE_OPTIONS=--openssl-legacy-provider
              yarn run build
            """
          }
        }
      }
    }

    stage('Deploy') {
      when {
        expression { env.BRANCH_NAME == 'main' }
      }
      steps {
        withCredentials([[
          $class: 'AmazonWebServicesCredentialsBinding',
          credentialsId: 'JekinsCredentialAWS',
          accessKeyVariable: 'AWS_ACCESS_KEY_ID',
          secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
        ]]) {
          script {
            if (isUnix()) {
              sh 'aws s3 sync build/ s3://$S3_BUCKET/ --delete --region $AWS_REGION'
            } else {
              bat """
                where aws
                aws s3 sync build/ s3://%S3_BUCKET%/ --delete --region %AWS_REGION%
              """
            }
          }
        }
      }
    }
  }

  post {
    always {
      script {
        if (isUnix()) {
          sh 'rm -f .env || true'
        } else {
          bat 'del /F .env 2> nul || exit 0'
        }
      }
    }
    success {
      echo "✅ Build successful!"
      script {
        if (isUnix()) {
          sh 'rm -rf node_modules build || true'
        } else {
          bat 'rd /s /q node_modules build 2> nul || exit 0'
        }
      }
    }
    failure {
      echo "❌ Build failed! Check logs at: ${env.BUILD_URL}"
    }
  }
}
