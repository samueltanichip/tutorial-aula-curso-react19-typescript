pipeline {
  agent any

  tools {
    nodejs "nodejs"
  }

  environment {
    NODE_OPTIONS = "--max-old-space-size=5120"
    CI = "false"
    AWS_REGION = 'us-east-1'
    S3_BUCKET = 'jenkinstest-x015r2'
    REACT_APP_CLIENT = 'Ionics'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Load environment variables') {
      steps {
        script {
          if (isUnix()) {
            withCredentials([file(credentialsId: 'mainChiptronicENV', variable: 'ENV_SECRET')]) {
              sh 'cp "$ENV_SECRET" .env'
            }
          } else {
            withCredentials([file(credentialsId: 'mainChiptronicENV', variable: 'ENV_SECRET')]) {
              bat 'copy /Y "%ENV_SECRET%" .env'
            }
          }
        }
      }
    }

    stage('Install dependencies') {
      steps {
        echo 'Installing dependencies'
        script {
          if (isUnix()) {
            sh 'yarn install --legacy-peer-deps --ignore-engines'
          } else {
            bat 'yarn install --legacy-peer-deps --ignore-engines'
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
            bat 'set NODE_OPTIONS=--openssl-legacy-provider && yarn run build'
          }
        }
      }
    }

    // stage('Deploy') {
    //   steps {
    //     withCredentials([[
    //       $class: 'AmazonWebServicesCredentialsBinding',
    //       credentialsId: 'JekinsCredentialAWS',
    //       accessKeyVariable: 'AWS_ACCESS_KEY_ID',
    //       secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
    //     ]]) {
    //       script {
    //         if (isUnix()) {
    //           sh 'aws s3 sync build/ s3://$S3_BUCKET/ --delete --region $AWS_REGION'
    //         } else {
    //           bat 'aws s3 sync build/ s3://%S3_BUCKET%/ --delete --region %AWS_REGION%'
    //         }
    //       }
    //     }
    //   }
    // }
  }

  post {
    always {
      script {
        if (isUnix()) {
          sh 'rm -f .env'
        } else {
          bat 'del /F .env'
        }
      }
    }
    success {
      echo "✅ Deployment successful."
      script {
        if (isUnix()) {
          sh 'rm -rf node_modules build'
        } else {
          bat 'rd /s /q node_modules build'
        }
      }
    }
    failure {
      echo "❌ Build failed! Check logs at: ${BUILD_URL}."
    }
  }
}
