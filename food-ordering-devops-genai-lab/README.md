# Food Ordering – End-to-End DevOps Practice Project

A beginner-friendly Spring Boot project for practising:

GitHub → Jenkins → Maven → SonarQube → Docker → Trivy → Amazon ECR
→ Terraform (AWS infrastructure) → Amazon EKS/Kubernetes → Helm (optional)
→ Prometheus/Grafana/CloudWatch (monitoring) → GenAI-assisted troubleshooting.

## 1. Project structure

- `application/`: Spring Boot Java application and tests
- `Dockerfile`: builds a container image from the Maven JAR
- `Jenkinsfile`: sample CI/CD pipeline
- `kubernetes/`: Deployment, Service, ConfigMap and HPA manifests
- `terraform/`: starter Terraform configuration for an EKS cluster
- `monitoring/`: starter notes for Prometheus/Grafana/CloudWatch
- `genai/`: safe prompts for AI-assisted troubleshooting

## 2. Prerequisites

For local practice:
- Java 21
- Maven 3.9+
- Git
- Docker Desktop

For AWS stages:
- AWS CLI configured with a practice IAM identity
- An ECR repository
- Terraform 1.5+
- kubectl
- An EKS cluster and permission to access it

For Jenkins stages:
- Jenkins with Git, Pipeline, Credentials Binding, SonarQube Scanner and Docker-related plugins
- Docker CLI/engine available to the Jenkins agent
- AWS CLI available to the Jenkins agent
- SonarQube server configured in Jenkins as `SonarQube`
- Jenkins credentials configured as described below

## 3. Run locally first

From the project root:

```bash
cd application
mvn clean test package
```

The JAR is created at:

```text
application/target/food-ordering-app.jar
```

Build the image from the project root (Dockerfile expects this JAR path):

```bash
docker build -t food-ordering-app:v1 .
```

Run it:

```bash
docker run --rm -d --name food-ordering-local -p 8080:8080 food-ordering-app:v1
```

Test in a browser:
- http://localhost:8080/health
- http://localhost:8080/api/menu

Stop the container:

```bash
docker stop food-ordering-local
```

## 4. Push to GitHub

Create an empty GitHub repository, then from this folder:

```bash
git init
git add .
git commit -m "Initial food ordering DevOps lab"
git branch -M main
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPO.git
git push -u origin main
```

Replace the URL with your own repository URL.

## 5. Jenkins configuration

Create these Jenkins credentials / settings before running the full pipeline:

1. `github-credentials` (only if your repository is private; username/token or SSH credential)
2. `aws-access-key-id` and `aws-secret-access-key` are intentionally NOT embedded in the Jenkinsfile. Prefer an attached IAM role on the Jenkins agent. If using credentials, adapt the pipeline to your organisation's approved credential method.
3. Configure a SonarQube server in **Manage Jenkins → System** with name `SonarQube`.
4. Configure the SonarQube token as Jenkins credential `sonarqube-token` if you use the optional scanner stage.
5. Ensure the Jenkins agent has Java 21, Maven, Docker, AWS CLI, kubectl and Trivy installed as needed.
6. Set the Jenkins environment variables in the job:
   - `AWS_ACCOUNT_ID`
   - `AWS_REGION` (example: `ap-south-1`)
   - `ECR_REPOSITORY` (example: `food-ordering-app`)
   - `EKS_CLUSTER_NAME` (only for deployment stage)

The included Jenkinsfile is a learning template. Review and adapt credentials, tool names, agent labels and deployment approvals before using it. Do not expose AWS keys or tokens in source control.

## 6. ECR setup

Create an ECR repository once:

```bash
aws ecr create-repository --repository-name food-ordering-app --region ap-south-1
```

The Jenkins agent needs permission to authenticate and push to ECR. Prefer an IAM role with least-privilege permissions.

The image URI format is:

```text
AWS_ACCOUNT_ID.dkr.ecr.AWS_REGION.amazonaws.com/food-ordering-app:BUILD_NUMBER
```

## 7. Terraform and EKS

Terraform files are a starter example using the official VPC and EKS modules. Review the variables and costs before applying.

```bash
cd terraform
terraform init
terraform fmt -check
terraform validate
terraform plan
terraform apply
```

**Cost warning:** EKS control plane, worker nodes, load balancers, NAT gateways and storage can incur AWS charges. Do not run `terraform apply` unless you understand the estimate. Destroy lab infrastructure when finished:

```bash
terraform destroy
```

After cluster creation, configure kubectl (replace placeholders):

```bash
aws eks update-kubeconfig --region ap-south-1 --name YOUR_EKS_CLUSTER_NAME
kubectl get nodes
```

## 8. Kubernetes deployment

Update `kubernetes/deployment.yaml` with your actual ECR image URI, then:

```bash
kubectl apply -f kubernetes/configmap.yaml
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
kubectl get pods
kubectl get svc
kubectl rollout status deployment/food-ordering
```

For a quick local cluster (Docker Desktop Kubernetes / kind / minikube), use a locally built image and adapt `imagePullPolicy` as appropriate.

The HPA requires a metrics API such as metrics-server and CPU requests on the container. Apply it only after those are available:

```bash
kubectl apply -f kubernetes/hpa.yaml
kubectl get hpa
```

## 9. Optional Helm practice

The `helm/food-ordering/` folder contains a starter chart:

```bash
helm lint helm/food-ordering
helm install food-ordering helm/food-ordering
helm upgrade food-ordering helm/food-ordering
helm history food-ordering
```

Set the image repository and tag in `helm/food-ordering/values.yaml` first.

## 10. Monitoring and GenAI practice

Read `monitoring/README.md` and `genai/prompts.md`.

Use GenAI to explain errors and suggest fixes, but verify every command, YAML change, IAM policy and Terraform plan yourself. Never paste secrets, tokens, passwords, customer data or private production logs into a public AI service.

## 11. Suggested learning order

1. Run Maven tests and build the JAR locally.
2. Build and run one Docker container locally; test `/health`.
3. Push the project to GitHub.
4. Configure Jenkins and run Checkout → Maven → SonarQube → Docker.
5. Push the Docker image to ECR.
6. Understand and validate Terraform; create AWS resources only after checking cost.
7. Deploy to EKS using Kubernetes YAML.
8. Practise Helm, HPA and monitoring.
9. Use the GenAI prompts to investigate a deliberately broken build or Pod.

Start with one Pod (`replicas: 1`) to understand deployment. Then change to 3 replicas and observe how Kubernetes maintains the desired count.
