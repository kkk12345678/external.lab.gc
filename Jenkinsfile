pipeline {
    agent any

    stages {
        stage  ('Scan with SonarQube') {
            steps {
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'SonarQubeToken') {
                    bat "./gradlew sonar"
                }
            }
        }

        stage("Code coverage") {     
	        steps {          
			    bat "./gradlew jacocoTestReport"          
			    publishHTML (target: [reportDir: 'build/reports/jacoco/test/html', reportFiles: 'index.html', reportName: "JaCoCo Report"])          
			    bat "./gradlew jacocoTestCoverageVerification"     
			}
        }
    }
}
