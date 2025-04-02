pipeline {
  agent any

  tools {
    nodejs "NodeJS-16.16.0"
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
        withCredentials([file(credentialsId: 'mainChiptronicENV', variable: 'ENV_SECRET')]) {
          sh 'cp "$ENV_SECRET" .env'
        }
      }
    }

    stage('Install dependencies') {
      steps {
        echo 'Installing dependencies'
        sh 'yarn install --legacy-peer-deps --ignore-engines'
      }
    }

    stage('Build') {
      steps {
        echo "Building branch ${env.BRANCH_NAME}"
        sh 'yarn run build --openssl-legacy-provider'
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
    //       sh 'aws s3 sync build/ s3://$S3_BUCKET/ --delete --region $AWS_REGION'
    //     }
    //   }
    // }
  }

  post {
    always {
      sh 'rm -f .env'
    }
    success {
      echo "✅ Deployment successful."
      sh 'rm -rf node_modules build'
    }
    failure {
      echo "❌ Build failed! Check logs at: ${BUILD_URL}."
    }
  }
}
