pipeline {
    agent any

    environment {
        SENTRY_AUTH_TOKEN = credentials('SENTRY_AUTH_TOKEN')
        SENTRY_PROJECT = credentials('SENTRY_PROJECT')
        SENTRY_ORG = credentials('SENTRY_ORG')
        SENTRY_DSN = credentials('SENTRY_DSN')
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout Backend') {
            steps {
                git url: 'https://github.com/diasel2000/estimator.git', branch: 'develop', credentialsId: 'Git'  // Переконайтесь, що Git облікові дані є
                sh 'chmod +x gradlew'
            }
        }
        stage('Build Backend') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Test Backend') {
            steps {
                sh './gradlew test'
            }
        }
        stage('Checkout Frontend') {
            steps {
                script {
                    dir('estimator-frontend') {
                        git url: 'https://github.com/diasel2000/estimator.git', branch: 'frontend', credentialsId: 'Git'  // Переконайтесь, що Git облікові дані є
                    }
                }
            }
        }
        stage('Build Frontend') {
            steps {
                script {
                    dir('estimator-frontend') {
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }
        stage('Upload to Sentry') {
                    steps {
                        script {
                            sh 'sentry-cli releases new $RELEASE_VERSION'
                            sh 'sentry-cli releases finalize $RELEASE_VERSION'
                        }
                    }
                }
//         stage('Deploy Frontend') {
//             steps {
//                 withCredentials([usernamePassword(credentialsId: 'FTP_CREDENTIALS_ID', passwordVariable: 'FTP_PASS', usernameVariable: 'FTP_USER')]) {
//                     sh '''
//                     curl --ftp-create-dirs -T estimator-frontend/dist/**/* \
//                     -u $FTP_USER:$FTP_PASS \
//                     ftp://$FTP_HOST$FTP_DIR/
//                     '''
//                 }
//             }
//         }
//         stage('Deploy Backend') {
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
