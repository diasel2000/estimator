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
//        TODO need add DEPLOYMENT stage
    }

   post {
       always {
           archiveArtifacts artifacts: '**/build/libs/*.jar', allowEmptyArchive: true
           junit '**/build/test-results/test/*.xml'
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
       unstable {
           mail to: 'estimateyouritapp@gmail.com',
                subject: "UNSTABLE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<h2>Build ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                         <p>Status: UNSTABLE</p>
                         <p>Details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                         <p>Commit: ${env.GIT_COMMIT}</p>
                         <p>Branch: ${env.BRANCH_NAME}</p>""",
                mimeType: 'text/html'
       }
       aborted {
           mail to: 'estimateyouritapp@gmail.com',
                subject: "ABORTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<h2>Build ${env.JOB_NAME} [${env.BUILD_NUMBER}]</h2>
                         <p>Status: ABORTED</p>
                         <p>Details: <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                         <p>Commit: ${env.GIT_COMMIT}</p>
                         <p>Branch: ${env.BRANCH_NAME}</p>""",
                mimeType: 'text/html'
       }
   }
}
