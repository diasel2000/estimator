pipeline {
    agent any

    environment {
        FTP_HOST = 'ftp.slategray-nightingale-123371.hostingersite.com'
        FTP_DIR = '/home/u440805526/domains/slategray-nightingale-123371.hostingersite.com/public_html'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/diasel2000/estimator.git', credentialsId: 'Git'
                sh 'chmod +x gradlew'
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
                withCredentials([usernamePassword(credentialsId: 'FTP_CREDENTIALS_ID', passwordVariable: 'FTP_PASS', usernameVariable: 'FTP_USER')]) {
                    sh '''
                    curl -T build/libs/*.jar ftp://$FTP_USER:$FTP_PASS@$FTP_HOST$FTP_DIR/ --ftp-create-dirs
                    '''
                }
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
