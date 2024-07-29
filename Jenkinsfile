pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/diasel2000/estimator.git', credentialsId: 'Git'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('Deploy') {
            steps {
                sh './gradlew deploy'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
            junit 'build/test-results/test/*.xml'
        }
        success {
            mail to: 'n221296@gmail.com',
                 subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                 body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' was successful."
        }
        failure {
            mail to: 'n221296@gmail.com',
                 subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                 body: "Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' failed."
        }
    }
}
