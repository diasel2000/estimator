pipeline {
    agent any

    environment {
        FTP_HOST = 'ftp.slategray-nightingale-123371.hostingersite.com'
        FTP_DIR = '/home/u440805526/domains/slategray-nightingale-123371.hostingersite.com/public_html'
    }

    triggers {
        githubPush()
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
        // TODO: Select client to deploy
                            // This stage is currently commented out until resolved,
                            // which client to use for deployment.
//         stage('Deploy') {
//             steps {
//                 withCredentials([usernamePassword(credentialsId: 'FTP_CREDENTIALS_ID', passwordVariable: 'FTP_PASS', usernameVariable: 'FTP_USER')]) {
//                     sh 'echo "Testing FTP connection with credentials:"'
//                     sh 'echo "FTP_USER: $FTP_USER"'
//                     sh 'echo "FTP_PASS: $FTP_PASS"'
//                     sh '''
//                     curl --ftp-create-dirs -T build/libs/*.jar \
//                     -u $FTP_USER:$FTP_PASS \
//                     ftp://$FTP_HOST$FTP_DIR/
//                     '''
//                 }
//             }
//         }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
            junit 'build/test-results/test/*.xml'
        }
        success {
            mail to: 'estimateyouritapp@gmail.com',
                 subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                 body: """<h2>Build ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                          <p>Status: SUCCESS</p>
                          <p>Details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                          <p>Commit: ${env.GIT_COMMIT}</p>
                          <p>Branch: ${env.BRANCH_NAME}</p>""",
                 mimeType: 'text/html'
        }
        failure {
            mail to: 'estimateyouritapp@gmail.com',
                 subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                 body: """<h2>Build ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                          <p>Status: FAILURE</p>
                          <p>Details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                          <p>Commit: ${env.GIT_COMMIT}</p>
                          <p>Branch: ${env.BRANCH_NAME}</p>""",
                 mimeType: 'text/html'
        }
    }
}
