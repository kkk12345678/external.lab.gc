pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                bat "./gradlew build"
            }
        }
        stage('JaCoCo') {
            steps {
                bat "./gradlew jacocoTestReport"
            }
        }
        stage('Scan with SonarQube') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'SonarQubeToken') {
                    bat "./gradlew sonar"
                }
            }
        }

        stage('Deploy') {
            steps {
                bat '''copy C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\SonarQube\\build\\libs\\gift-certificate-0.0.1-SNAPSHOT D:\\mdocs\\programming\\Programs\\apache-tomcat-8.5.81\\webapps\\gc-app.war'''
            }
        }
    }
}
