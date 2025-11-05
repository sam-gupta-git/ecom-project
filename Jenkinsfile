pipeline {
    agent any
    
    environment {
        S3_BUCKET = 'trng2309-1'
        SECRET_BUCKET = 'kyles-secret-bucket'
        AWS_REGION = 'us-east-2'
        DOCKER_IMAGE = 'spring-backend-01'
        EXTERNAL_PORT = '8081'
        INTERNAL_PORT = '8080'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/revature-sam/ecom-project'
            }
        }

        stage('Fetch Secrets') {
            steps {
                sh 'aws s3 cp s3://${SECRET_BUCKET}/team1/application.properties revagenda-server/src/main/resources/'
            }
        }
        
        stage('Build Spring Backend') {
            steps {
                dir('revagenda-server') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        
        stage('Cleanup Old Docker Resources') {
            steps {
                sh '''
                    docker stop ${DOCKER_IMAGE} || true
                    docker rm ${DOCKER_IMAGE} || true
                    docker system prune -a -f
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} .'
                sh 'docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest'
            }
        }
        
        stage('Deploy Backend Container') {
            steps {
                sh 'docker run -d --name ${DOCKER_IMAGE} -p ${EXTERNAL_PORT}:${INTERNAL_PORT} ${DOCKER_IMAGE}:latest'
            }
        }
    }
    
    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}