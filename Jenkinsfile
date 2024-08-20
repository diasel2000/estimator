pipeline {
    agent any

    environment {
        SENTRY_AUTH_TOKEN = credentials('SENTRY_AUTH_TOKEN')
        SENTRY_PROJECT = credentials('SENTRY_PROJECT')
        SENTRY_ORG = credentials('SENTRY_ORG')
        SENTRY_DSN = credentials('SENTRY_DSN')
        EMAIL_RECIPIENTS = 'estimateyouritapp@gmail.com'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout Backend') {
            steps {
                git url: 'https://github.com/diasel2000/estimator.git', branch: 'develop', credentialsId: 'Git'
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
        stage('Build Frontend') {
            steps {
                dir('estimator-frontend') {
                    sh 'npm install'
                    sh 'npm run build'
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
               script {
                   echo 'Archiving artifacts and test results...'
               }
               archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
               junit 'build/test-results/test/*.xml'
               archiveArtifacts artifacts: '**/build/logs/*.log', allowEmptyArchive: true
               echo "Artifacts and logs archived."
           }
           success {
               script {
                   def duration = currentBuild.durationString.replace(' and counting', '')
                   def buildUser = currentBuild.getCause(hudson.model.Cause$UserIdCause)?.getUserName() ?: "Automated Trigger"
                   def buildMessage = """
                       <h2>Build Success: ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                       <ul>
                           <li><strong>Status:</strong> <span style="color:green;">SUCCESS</span></li>
                           <li><strong>Project:</strong> ${env.JOB_NAME}</li>
                           <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                           <li><strong>Branch:</strong> ${env.BRANCH_NAME}</li>
                           <li><strong>Commit ID:</strong> ${env.GIT_COMMIT}</li>
                           <li><strong>Duration:</strong> ${duration}</li>
                           <li><strong>Triggered by:</strong> ${buildUser}</li>
                           <li><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></li>
                       </ul>
                       <h3>Artifacts:</h3>
                       <ul>
                           <li>JAR file: <a href="${env.BUILD_URL}artifact/build/libs/">Download</a></li>
                           <li>Test Reports: <a href="${env.BUILD_URL}testReport/">View</a></li>
                           <li>Build Logs: <a href="${env.BUILD_URL}artifact/build/logs/">View</a></li>
                       </ul>
                       """
                   mail to: EMAIL_RECIPIENTS,
                        subject: "SUCCESS: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                        body: buildMessage,
                        mimeType: 'text/html'
                   echo "Success email sent."
               }
           }
           failure {
               script {
                   def duration = currentBuild.durationString.replace(' and counting', '')
                   def buildUser = currentBuild.getCause(hudson.model.Cause$UserIdCause)?.getUserName() ?: "Automated Trigger"
                   def buildMessage = """
                       <h2>Build Failure: ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                       <ul>
                           <li><strong>Status:</strong> <span style="color:red;">FAILURE</span></li>
                           <li><strong>Project:</strong> ${env.JOB_NAME}</li>
                           <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                           <li><strong>Branch:</strong> ${env.BRANCH_NAME}</li>
                           <li><strong>Commit ID:</strong> ${env.GIT_COMMIT}</li>
                           <li><strong>Duration:</strong> ${duration}</li>
                           <li><strong>Triggered by:</strong> ${buildUser}</li>
                           <li><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></li>
                       </ul>
                       <h3>Error Analysis:</h3>
                       <p>Please review the following details to troubleshoot the issue:</p>
                       <ul>
                           <li>Check the <a href="${env.BUILD_URL}console">console output</a> for errors.</li>
                           <li>Review the <a href="${env.BUILD_URL}artifact/build/logs/">build logs</a> for more insights.</li>
                           <li>If the error is related to code, inspect the <a href="${env.BUILD_URL}artifact/build/libs/">JAR file</a> (if available).</li>
                           <li>Contact the last committer: <strong>${buildUser}</strong></li>
                       </ul>
                       """
                   mail to: EMAIL_RECIPIENTS,
                        subject: "FAILURE: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                        body: buildMessage,
                        mimeType: 'text/html'
                   echo "Failure email sent."
               }
           }
           unstable {
               script {
                   def duration = currentBuild.durationString.replace(' and counting', '')
                   def buildUser = currentBuild.getCause(hudson.model.Cause$UserIdCause)?.getUserName() ?: "Automated Trigger"
                   def buildMessage = """
                       <h2>Build Unstable: ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                       <ul>
                           <li><strong>Status:</strong> <span style="color:orange;">UNSTABLE</span></li>
                           <li><strong>Project:</strong> ${env.JOB_NAME}</li>
                           <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                           <li><strong>Branch:</strong> ${env.BRANCH_NAME}</li>
                           <li><strong>Commit ID:</strong> ${env.GIT_COMMIT}</li>
                           <li><strong>Duration:</strong> ${duration}</li>
                           <li><strong>Triggered by:</strong> ${buildUser}</li>
                           <li><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></li>
                       </ul>
                       <h3>Details:</h3>
                       <p>The build completed but with some issues. Please review the test results and logs for more information.</p>
                       <ul>
                           <li>Test Reports: <a href="${env.BUILD_URL}testReport/">View</a></li>
                           <li>Build Logs: <a href="${env.BUILD_URL}artifact/build/logs/">View</a></li>
                       </ul>
                       """
                   mail to: EMAIL_RECIPIENTS,
                        subject: "UNSTABLE: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                        body: buildMessage,
                        mimeType: 'text/html'
                   echo "Unstable build email sent."
               }
           }
           aborted {
               script {
                   def duration = currentBuild.durationString.replace(' and counting', '')
                   def buildUser = currentBuild.getCause(hudson.model.Cause$UserIdCause)?.getUserName() ?: "Automated Trigger"
                   def buildMessage = """
                       <h2>Build Aborted: ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                       <ul>
                           <li><strong>Status:</strong> <span style="color:gray;">ABORTED</span></li>
                           <li><strong>Project:</strong> ${env.JOB_NAME}</li>
                           <li><strong>Build Number:</strong> ${env.BUILD_NUMBER}</li>
                           <li><strong>Branch:</strong> ${env.BRANCH_NAME}</li>
                           <li><strong>Commit ID:</strong> ${env.GIT_COMMIT}</li>
                           <li><strong>Duration:</strong> ${duration}</li>
                           <li><strong>Triggered by:</strong> ${buildUser}</li>
                           <li><strong>Build URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></li>
                       </ul>
                       <h3>Details:</h3>
                       <p>The build was aborted. No further action is required unless the abort was unintentional.</p>
                       """
                   mail to: EMAIL_RECIPIENTS,
                        subject: "ABORTED: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
                        body: buildMessage,
                        mimeType: 'text/html'
                   echo "Aborted build email sent."
               }
           }
       }
}
