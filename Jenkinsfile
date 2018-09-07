pipeline {
    agent none
    stages {
        stage('Gradle Build') {
            agent  { label 'master' }
            steps {
                sh './gradlew clean'
                sh './gradlew build'
                stash 'workspace'
            }
        }
        stage('Build Container & Push to ACR') {
            agent { label 'master' }
            steps {
                unstash 'workspace'
                script {
                    docker.withRegistry("${env.DOCKER_REGISTRY_URL}", 'docker_registry_credentials') {
                        def customImage = docker.build("lpg-report-service:${env.BUILD_ID}")
                        customImage.push()
                    }
                }
            }
        }
        stage('Deploy to Integration?')  {
            agent none
            steps {
                script {
                    def deployToIntegration = input(message: 'Deploy to Integration?', ok: 'Yes')
                }
            }
        }
        stage('Deploy to Integration') {
            agent { label 'master' }
            steps {
                script {
                    def tfHome = tool name: 'Terraform', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
                    env.PATH = "${tfHome}:${env.PATH}"
                }
                withCredentials([
                    string(credentialsId: 'SECURE_FILES', variable: 'SF'),
                    usernamePassword(credentialsId: 'docker_registry_credentials', usernameVariable: 'acr_username', passwordVariable: 'acr_password')
                    ]) {
                    sh "set +e; rm -rf lpg-terraform-paas"
                    sh "git clone https://github.com/Civil-Service-Human-Resources/lpg-terraform-paas.git -b acrmodules --single-branch"
                    dir("lpg-terraform-paas/environments/master") {
                        sh "ln -s ${SF}/azure/cabinet-azure/00-integration/state.tf state.tf"
                        sh "ln -s ${SF}/azure/cabinet-azure/00-integration/integration-vars.tf integration-vars.tf"
                        sh "ln -s ../00-integration/00-vars.tf 00-vars.tf"
                        sh "terraform --version"
                        sh "terraform init"
                        sh "terraform validate"
                        sh "terraform plan -target=module.lpg-report-service -var 'lpg_report_service_docker_tag=${env.BUILD_ID}' -var 'docker_registry_server_username=${acr_username}' -var 'docker_registry_server_password=${acr_password}'"
                        sh "terraform apply -target=module.lpg-report-service -var 'lpg_report_service_docker_tag=${env.BUILD_ID}' -var 'docker_registry_server_username=${acr_username}' -var 'docker_registry_server_password=${acr_password}' -auto-approve"
                    }
                }
            }
        }
        /* disabled
        stage('Deploy to Staging?')  {
            agent none
            steps {
                script {
                    def deployToStaging = input(message: 'Deploy to Staging?', ok: 'Yes')
                }
            }
        }
        stage('Deploy to Staging') {
            agent { label 'master' }
            steps {
                script {
                    def tfHome = tool name: 'Terraform', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
                    env.PATH = "${tfHome}:${env.PATH}"
                }
                withCredentials([
                    string(credentialsId: 'SECURE_FILES', variable: 'SF'),
                    usernamePassword(credentialsId: 'docker_registry_credentials', usernameVariable: 'acr_username', passwordVariable: 'acr_password')
                    ]) {
                    sh "set +e; rm -rf lpg-terraform-paas"
                    sh "git clone https://github.com/Civil-Service-Human-Resources/lpg-terraform-paas.git -b acrmodules --single-branch"
                    dir("lpg-terraform-paas/environments/master") {
                        sh "ln -s ${SF}/azure/cabinet-azure/00-staging/state.tf state.tf"
                        sh "ln -s ${SF}/azure/cabinet-azure/00-staging/integration-vars.tf integration-vars.tf"
                        sh "ln -s ../00-staging/00-vars.tf 00-vars.tf"
                        sh "terraform --version"
                        sh "terraform init"
                        sh "terraform validate"
                        sh "terraform plan -target=module.lpg-report-service -var 'lpg_report_service_docker_tag=${env.BUILD_ID}' -var 'docker_registry_server_username=${acr_username}' -var 'docker_registry_server_password=${acr_password}'"
                        sh "terraform apply -target=module.lpg-report-service -var 'lpg_report_service_docker_tag=${env.BUILD_ID}' -var 'docker_registry_server_username=${acr_username}' -var 'docker_registry_server_password=${acr_password}' -auto-approve"
                    }
                }
            }
        }
        stage('Deploy to Production?')  {
            agent none
            steps {
                script {
                    def deployToProduction = input(message: 'Deploy to Production?', ok: 'Yes')
                }
            }
        }
        stage('Deploy to Production') {
            agent { label 'master' }
            steps {
                script {
                    def tfHome = tool name: 'Terraform', type: 'com.cloudbees.jenkins.plugins.customtools.CustomTool'
                    env.PATH = "${tfHome}:${env.PATH}"
                }
                withCredentials([
                    string(credentialsId: 'SECURE_FILES', variable: 'SF'),
                    usernamePassword(credentialsId: 'docker_registry_credentials', usernameVariable: 'acr_username', passwordVariable: 'acr_password')
                    ]) {
                    sh "set +e; rm -rf lpg-terraform-paas"
                    sh "git clone https://github.com/Civil-Service-Human-Resources/lpg-terraform-paas.git -b acrmodules --single-branch"
                    dir("lpg-terraform-paas/environments/master") {
                        sh "ln -s ${SF}/azure/cabinet-azure/00-production/state.tf state.tf"
                        sh "ln -s ${SF}/azure/cabinet-azure/00-production/integration-vars.tf integration-vars.tf"
                        sh "ln -s ../00-production/00-vars.tf 00-vars.tf"
                        sh "terraform --version"
                        sh "terraform init"
                        sh "terraform validate"
                        sh "terraform plan -target=module.lpg-report-service -var 'lpg_report_service_docker_tag=${env.BUILD_ID}' -var 'docker_registry_server_username=${acr_username}' -var 'docker_registry_server_password=${acr_password}'"
                        sh "terraform apply -target=module.lpg-report-service -var 'lpg_report_service_docker_tag=${env.BUILD_ID}' -var 'docker_registry_server_username=${acr_username}' -var 'docker_registry_server_password=${acr_password}' -auto-approve"
                    }
                }
            }
        }
        disabled */
    }
}