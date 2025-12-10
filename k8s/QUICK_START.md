# Quick Start Guide - GKE Deployment

Get your Airline Management App running on Google Kubernetes Engine in minutes!

## 🚀 Prerequisites

- GCP account with a project
- `gcloud` CLI installed and authenticated
- `kubectl` installed
- GKE cluster created (or use the commands below)

## ⚡ Quick Setup

### Step 1: Create GKE Cluster

```bash
# Set your variables
export PROJECT_ID="your-project-id"
export CLUSTER_NAME="airline-app-cluster"
export REGION="us-central1"

# Create cluster
gcloud container clusters create $CLUSTER_NAME \
  --project=$PROJECT_ID \
  --region=$REGION \
  --num-nodes=2 \
  --machine-type=e2-medium \
  --enable-autoscaling \
  --min-nodes=1 \
  --max-nodes=3

# Get credentials
gcloud container clusters get-credentials $CLUSTER_NAME \
  --region=$REGION \
  --project=$PROJECT_ID
```

### Step 2: Update Image URL

Edit `app-deployment.yaml` and replace the image URL (line 25):

```yaml
image: us-central1-docker.pkg.dev/YOUR_PROJECT_ID/YOUR_REPO/airline-app-image:TAG
```

### Step 3: Deploy Application

**Option A: Using the deployment script (Recommended)**

```bash
cd k8s
./deploy.sh
# Select option 1 to deploy all resources
```

**Option B: Manual deployment**

```bash
cd k8s
kubectl apply -f namespace.yaml
kubectl apply -f configmap.yaml
kubectl apply -f secret.yaml
kubectl apply -f mysql-pvc.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f mysql-service.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml
```

**Option C: Using Kustomize**

```bash
cd k8s
kubectl apply -k .
```

### Step 4: Wait for Deployment

```bash
# Watch pod status
kubectl get pods -n airline-app -w

# Or check overall status
kubectl get all -n airline-app
```

### Step 5: Get Application URL

```bash
# Get external IP
kubectl get svc airline-app-service -n airline-app

# Or use the script
./deploy.sh  # Select option 4
```

Wait for `EXTERNAL-IP` to be assigned (2-5 minutes).

### Step 6: Test the Application

```bash
# Set the external IP
export APP_URL=$(kubectl get svc airline-app-service -n airline-app -o jsonpath='{.status.loadBalancer.ingress[0].ip}')

# Test API
curl http://$APP_URL/flight/

# Create a flight
curl -X POST http://$APP_URL/flight/ \
  -H "Content-Type: application/json" \
  -d '{
    "flightName": "Air India 101",
    "source": "Mumbai",
    "destination": "Delhi",
    "ticketPrice": 5500.00
  }'

# Get all flights
curl http://$APP_URL/flight/
```

## 🔍 Common Commands

### Check Status

```bash
# All resources
kubectl get all -n airline-app

# Pods only
kubectl get pods -n airline-app

# Services with external IP
kubectl get svc -n airline-app
```

### View Logs

```bash
# Application logs
kubectl logs -n airline-app -l app=airline-management -f

# MySQL logs
kubectl logs -n airline-app -l app=mysql -f
```

### Scale Application

```bash
# Scale to 3 replicas
kubectl scale deployment airline-app --replicas=3 -n airline-app
```

### Access MySQL Database

```bash
# Port forward
kubectl port-forward svc/mysql-service 3306:3306 -n airline-app

# In another terminal, connect using mysql client
mysql -h 127.0.0.1 -P 3306 -u root -p
# Password: root (or your custom password)
```

### Debug Issues

```bash
# Describe pod
kubectl describe pod <pod-name> -n airline-app

# Get events
kubectl get events -n airline-app --sort-by='.lastTimestamp'

# Exec into pod
kubectl exec -it deployment/airline-app -n airline-app -- /bin/bash
```

## 🧹 Cleanup

### Delete Application Only

```bash
kubectl delete -f app-deployment.yaml
kubectl delete -f app-service.yaml
```

### Delete Everything

```bash
# Delete namespace (removes all resources)
kubectl delete namespace airline-app

# Or use the script
./deploy.sh  # Select option 7
```

### Delete GKE Cluster

```bash
gcloud container clusters delete $CLUSTER_NAME \
  --region=$REGION \
  --project=$PROJECT_ID \
  --quiet
```

## 🔐 Security Notes

⚠️ **IMPORTANT**: The default credentials in `secret.yaml` are:
- Username: `root`
- Password: `root`

**For production:**

1. Generate new base64 credentials:
   ```bash
   echo -n 'your-username' | base64
   echo -n 'your-password' | base64
   ```

2. Update `secret.yaml` with your encoded values

3. Consider using Google Secret Manager instead

## 📊 Resource Requirements

- **MySQL**: 512Mi-1Gi RAM, 250m-500m CPU, 10Gi storage
- **Application**: 512Mi-1Gi RAM, 250m-1000m CPU
- **Minimum cluster**: 2 nodes with e2-medium (2 vCPU, 4GB RAM each)

## 🔄 Update Application

```bash
# Update image
kubectl set image deployment/airline-app \
  airline-app=NEW_IMAGE_URL \
  -n airline-app

# Or edit deployment
kubectl edit deployment airline-app -n airline-app

# Watch rollout
kubectl rollout status deployment/airline-app -n airline-app
```

## 📚 Additional Files

- `README.md` - Comprehensive documentation
- `app-hpa.yaml` - Horizontal Pod Autoscaler (optional)
- `app-ingress.yaml` - Ingress for custom domain (optional)
- `deploy.sh` - Interactive deployment script

## 💡 Tips

1. **First deployment takes longer** - MySQL needs to initialize
2. **LoadBalancer IP assignment** - Can take 2-5 minutes
3. **Monitor costs** - LoadBalancer and persistent disks incur charges
4. **Use Ingress** - For production, use Ingress instead of LoadBalancer
5. **Enable autoscaling** - Deploy `app-hpa.yaml` for auto-scaling

## ❓ Troubleshooting

**Pods stuck in Pending:**
- Check PVC status: `kubectl get pvc -n airline-app`
- Check events: `kubectl get events -n airline-app`

**Can't connect to application:**
- Verify LoadBalancer has external IP
- Check if pods are running: `kubectl get pods -n airline-app`
- View logs: `kubectl logs -n airline-app -l app=airline-management`

**Database connection failed:**
- Ensure MySQL pod is running
- Check MySQL logs: `kubectl logs -n airline-app -l app=mysql`
- Verify service exists: `kubectl get svc mysql-service -n airline-app`

## 🎉 Success!

Once deployed, your Airline Management API will be accessible at:
- `http://<EXTERNAL-IP>/flight/` - API endpoint

For detailed documentation, see [README.md](README.md)

---

**Need help?** Check the full [README.md](README.md) for comprehensive documentation.
