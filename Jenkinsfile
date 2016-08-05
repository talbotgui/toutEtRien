#!groovy

node {
    stage 'Checkout'
    git branch: 'miniBootRestJpa', url: 'https://github.com/talbotgui/toutEtRien.git'
    def mvnHome = tool 'M3'

    stage 'Build'
    sh "${mvnHome}/bin/mvn clean install -Dmaven.test.skip=true"

    stage 'Unit test'
    sh "${mvnHome}/bin/mvn clean test-compile surefire:test"
    step([$class: 'JUnitResultArchiver', testResults: '**/TEST-*Test.xml'])

    stage 'Quality'
    sh "${mvnHome}/bin/mvn site -Dmaven.test.skip=true"
    step([$class: 'FindBugsPublisher'])
    step([$class: 'CheckStylePublisher'])
    step([$class: 'AnalysisPublisher'])
    step([$class: 'JavadocArchiver', javadocDir: 'target/site/apidocs', keepAll: false])
}

stage 'Approve'
timeout(time:1, unit:'DAYS') {
	input message:'Go to production?'
}

node {
    stage 'Production'
}

