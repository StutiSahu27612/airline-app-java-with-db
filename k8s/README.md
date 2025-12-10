# Kubernetes Deployment for Airline Management System

This directory contains Kubernetes manifest files for deploying the Airline Management System on Google Kubernetes Engine (GKE).

## 📋 Overview

The application consists of:
- **Spring Boot Application**: RESTful API for flight management (Port 9000)
- **MySQL Database**: Persistent data storage (Port 3306)

## 📁 Manifest Files

| File | Description |
|------|-------------|
| `namespace.yaml` | Creates the `airline-app` namespace |
| `configmap.yaml` | Application configuration (non-sensitive data) |
| `secret.yaml` | Database credentials (base64 encoded) |
| `mysql-pvc.yaml` | Persistent Volume Claim for MySQL data |
| `mysql-deployment.yaml` | MySQL 8.0 database deployment |
| `mysql-service.yaml` | ClusterIP service for MySQL |
| `app-deployment.yaml` | Airline application deployment |
| `app-service.yaml` | LoadBalancer service for external access |
| `kustomization.yaml` | Kustomize configuration file |

## 🚀 Prerequisites

Before deploying, ensure you have:

1. **Google Cloud Project** with GKE enabled
2. **GKE Cluster** created and running
3. **kubectl** installed and configured
4. **gcloud CLI** installed and authenticated
5. **Docker image** pushed to GCP Artifact Registry

### Create GKE Cluster (if not exists)

```bash
# Set your project ID
export PROJECT_ID="your-gcp-project-id"
export CLUSTER_NAME="airline-app-cluster"
export REGION="us-central1"

# Create GKE cluster
gcloud container clusters create $CLUSTER_NAME \
  --project=$PROJECT_ID \
  --region=$REGION \
  --num-nodes=2 \
  --machine-type=e2-medium \
  --enable-autoscaling \
  --min-nodes=1 \
  --max-nodes=3 \
  --enable-autorepair \
  --enable-autoupgrade

# Get cluster credentials
gcloud container clusters get-credentials $CLUSTER_NAME \
  --region=$REGION \
  --project=$PROJECT_ID
```

## 🔧 Configuration

### 1. Update Docker Image

Edit `app-deployment.yaml` and replace the image URL with your actual image:

```yaml
image: us-central1-docker.pkg.dev/YOUR_PROJECT_ID/YOUR_REPO/airline-app-image:TAG
```

Or use the image from your CI/CD pipeline.

### 2. Update Secrets

**IMPORTANT**: Change the default database credentials for production!

Generate base64 encoded values:

```bash
# Generate base64 encoded credentials
echo -n 'your-username' | base64
echo -n 'your-password' | base64
```

Update `secret.yaml` with your encoded values:

```yaml
data:
  DB_USERNAME: <base64-encoded-username>
  DB_PASSWORD: <base64-encoded-password>
  MYSQL_ROOT_PASSWORD: <base64-encoded-password>
```

### 3. Configure Storage Class (Optional)

For GKE, you can specify a storage class in `mysql-pvc.yaml`:

```yaml
spec:
  storageClassName: standard-rwo  # or premium-rwo for SSD
```

## 📦 Deployment

### Option 1: Deploy All Manifests at Once

```bash
# Navigate to k8s directory
cd k8s

# Apply all manifests
kubectl apply -f .

# Or deploy in specific order
kubectl apply -f namespace.yaml
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml
kubectl apply -f mysql-pvc.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f mysql-service.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml
```

### Option 2: Deploy Using Kustomize

```bash
# Deploy using kustomize
kubectl apply -k k8s/

# Or use kubectl kustomize
kubectl kustomize k8s/ | kubectl apply -f -
```

## 🔍 Verification

### Check Deployment Status

```bash
# Check all resources in the namespace
kubectl get all -n airline-app

# Check pods status
kubectl get pods -n airline-app

# Check services
kubectl get svc -n airline-app

# Check persistent volume claim
kubectl get pvc -n airline-app
```

### Monitor Deployment

```bash
# Watch pod status
kubectl get pods -n airline-app -w

# View pod logs
kubectl logs -n airline-app deployment/airline-app -f
kubectl logs -n airline-app deployment/mysql -f

# Describe resources for troubleshooting
kubectl describe pod -n airline-app <pod-name>
kubectl describe deployment -n airline-app airline-app
```

### Access the Application

```bash
# Get the external IP of the LoadBalancer
kubectl get svc airline-app-service -n airline-app

# Wait for EXTERNAL-IP to be assigned (may take a few minutes)
# Access the application at: http://<EXTERNAL-IP>/flight/
```

## 🧪 Testing the API

Once the LoadBalancer has an external IP:

```bash
# Set the external IP
export EXTERNAL_IP=$(kubectl get svc airline-app-service -n airline-app -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

# Test the API
curl http://$EXTERNAL_IP/flight/

# Create a flight
curl -X POST http://$EXTERNAL_IP/flight/ \
  -H "Content-Type: application/json" \
  -d '{
    "flightName": "Air India 101",
    "source": "Mumbai",
    "destination": "Delhi",
    "ticketPrice": 5500.00
  }'

# Get all flights
curl http://$EXTERNAL_IP/flight/
```

## 🔄 Updates and Rollbacks

### Update Application

```bash
# Update the image in deployment
kubectl set image deployment/airline-app \
  airline-app=us-central1-docker.pkg.dev/PROJECT_ID/REPO/airline-app-image:NEW_TAG \
  -n airline-app

# Or edit deployment directly
kubectl edit deployment airline-app -n airline-app

# Watch rollout status
kubectl rollout status deployment/airline-app -n airline-app
```

### Rollback Deployment

```bash
# View rollout history
kubectl rollout history deployment/airline-app -n airline-app

# Rollback to previous version
kubectl rollout undo deployment/airline-app -n airline-app

# Rollback to specific revision
kubectl rollout undo deployment/airline-app --to-revision=2 -n airline-app
```

## 📊 Scaling

### Manual Scaling

```bash
# Scale application pods
kubectl scale deployment airline-app --replicas=3 -n airline-app

# Verify scaling
kubectl get pods -n airline-app
```

### Auto-scaling (HPA)

```bash
# Create Horizontal Pod Autoscaler
kubectl autoscale deployment airline-app \
  --cpu-percent=70 \
  --min=2 \
  --max=10 \
  -n airline-app

# Check HPA status
kubectl get hpa -n airline-app
```

## 🐛 Troubleshooting

### Common Issues

**Pods not starting:**
```bash
# Check pod events
kubectl describe pod <pod-name> -n airline-app

# Check logs
kubectl logs <pod-name> -n airline-app

# Check if image exists and is accessible
kubectl get events -n airline-app --sort-by='.lastTimestamp'
```

**Database connection issues:**
```bash
# Check MySQL pod
kubectl get pods -n airline-app | grep mysql

# Exec into application pod and test MySQL connection
kubectl exec -it <app-pod-name> -n airline-app -- /bin/sh
# Then try: nc -zv mysql-service 3306
```

**LoadBalancer not getting external IP:**
```bash
# Check service status
kubectl describe svc airline-app-service -n airline-app

# On GKE, this might take 2-5 minutes
# If stuck, check GCP Console for Load Balancer status
```

### Access MySQL Database

```bash
# Port forward to MySQL
kubectl port-forward svc/mysql-service 3306:3306 -n airline-app

# Connect using mysql client
mysql -h 127.0.0.1 -P 3306 -u root -p
# Password: root (or your custom password)
```

### Debug Application Pod

```bash
# Exec into application pod
kubectl exec -it deployment/airline-app -n airline-app -- /bin/bash

# Check environment variables
kubectl exec deployment/airline-app -n airline-app -- env | grep SPRING
```

## 🧹 Cleanup

### Delete All Resources

```bash
# Delete all resources in namespace
kubectl delete namespace airline-app

# Or delete using kustomize
kubectl delete -k k8s/

# Or delete individual resources
kubectl delete -f k8s/
```

### Delete GKE Cluster

```bash
gcloud container clusters delete $CLUSTER_NAME \
  --region=$REGION \
  --project=$PROJECT_ID \
  --quiet
```

## 🔐 Security Best Practices

1. **Change Default Credentials**: Update database passwords in `secret.yaml`
2. **Use Secrets Manager**: Consider using Google Secret Manager instead of Kubernetes secrets
3. **Network Policies**: Implement network policies to restrict pod communication
4. **RBAC**: Set up proper Role-Based Access Control
5. **Image Scanning**: Scan Docker images for vulnerabilities before deployment
6. **TLS/SSL**: Use Ingress with TLS for HTTPS access
7. **Resource Limits**: Always set resource requests and limits

## 📈 Production Recommendations

1. **Use Ingress** instead of LoadBalancer for better control and SSL termination
2. **Enable Cloud SQL** instead of MySQL pod for production databases
3. **Set up Monitoring** using Google Cloud Monitoring or Prometheus
4. **Configure Logging** with Cloud Logging or ELK stack
5. **Implement Backup Strategy** for MySQL data
6. **Use ConfigMaps/Secrets from Cloud Secret Manager**
7. **Set up CI/CD Pipeline** for automated deployments
8. **Configure Health Checks** properly
9. **Enable Pod Disruption Budgets** for high availability

## 📚 Additional Resources

- [GKE Documentation](https://cloud.google.com/kubernetes-engine/docs)
- [Kubernetes Documentation](https://kubernetes.io/docs/home/)
- [Kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)
- [GCP Artifact Registry](https://cloud.google.com/artifact-registry/docs)

## 📞 Support

For issues or questions, please check the main repository README or open an issue.

---

**Happy Deploying! ✈️ ☸️**
