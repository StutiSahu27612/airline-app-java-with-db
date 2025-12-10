#!/bin/bash

#############################################
# Airline App - GKE Deployment Script
#############################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Helper functions
info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if kubectl is installed
check_kubectl() {
    if ! command -v kubectl &> /dev/null; then
        error "kubectl is not installed. Please install kubectl first."
        exit 1
    fi
    success "kubectl is installed"
}

# Check if connected to a cluster
check_cluster_connection() {
    if ! kubectl cluster-info &> /dev/null; then
        error "Not connected to a Kubernetes cluster. Please configure kubectl first."
        exit 1
    fi
    success "Connected to Kubernetes cluster"
}

# Display menu
show_menu() {
    echo ""
    echo "=========================================="
    echo "  Airline App - GKE Deployment Menu"
    echo "=========================================="
    echo "1. Deploy all resources"
    echo "2. Deploy using Kustomize"
    echo "3. Check deployment status"
    echo "4. Get application URL"
    echo "5. View logs"
    echo "6. Scale application"
    echo "7. Delete all resources"
    echo "8. Exit"
    echo "=========================================="
    echo -n "Enter your choice [1-8]: "
}

# Deploy all resources
deploy_all() {
    info "Deploying all Kubernetes resources..."
    
    kubectl apply -f namespace.yaml
    kubectl apply -f configmap.yaml
    kubectl apply -f secret.yaml
    kubectl apply -f mysql-pvc.yaml
    kubectl apply -f mysql-deployment.yaml
    kubectl apply -f mysql-service.yaml
    kubectl apply -f app-deployment.yaml
    kubectl apply -f app-service.yaml
    
    success "All resources deployed successfully!"
    
    info "Waiting for pods to be ready..."
    kubectl wait --for=condition=ready pod -l app=mysql -n airline-app --timeout=120s || warning "MySQL pod may still be starting"
    kubectl wait --for=condition=ready pod -l app=airline-management -n airline-app --timeout=180s || warning "Application pods may still be starting"
    
    echo ""
    info "Deployment complete! Run option 3 to check status."
}

# Deploy using Kustomize
deploy_kustomize() {
    info "Deploying using Kustomize..."
    
    kubectl apply -k .
    
    success "Resources deployed via Kustomize!"
    
    info "Waiting for pods to be ready..."
    kubectl wait --for=condition=ready pod -l app=mysql -n airline-app --timeout=120s || warning "MySQL pod may still be starting"
    kubectl wait --for=condition=ready pod -l app=airline-management -n airline-app --timeout=180s || warning "Application pods may still be starting"
}

# Check deployment status
check_status() {
    info "Checking deployment status..."
    echo ""
    
    echo "=== Namespaces ==="
    kubectl get namespace airline-app
    echo ""
    
    echo "=== All Resources in airline-app namespace ==="
    kubectl get all -n airline-app
    echo ""
    
    echo "=== Persistent Volume Claims ==="
    kubectl get pvc -n airline-app
    echo ""
    
    echo "=== ConfigMaps and Secrets ==="
    kubectl get configmap,secret -n airline-app
    echo ""
}

# Get application URL
get_app_url() {
    info "Fetching application URL..."
    echo ""
    
    EXTERNAL_IP=$(kubectl get svc airline-app-service -n airline-app -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null)
    
    if [ -z "$EXTERNAL_IP" ] || [ "$EXTERNAL_IP" == "null" ]; then
        warning "External IP not yet assigned. It may take a few minutes."
        info "Run this command to check: kubectl get svc airline-app-service -n airline-app"
    else
        success "Application is accessible at:"
        echo ""
        echo "  Base URL: http://$EXTERNAL_IP"
        echo "  API Endpoint: http://$EXTERNAL_IP/flight/"
        echo ""
        info "Test the API with:"
        echo "  curl http://$EXTERNAL_IP/flight/"
    fi
}

# View logs
view_logs() {
    echo ""
    echo "Select logs to view:"
    echo "1. Application logs"
    echo "2. MySQL logs"
    echo "3. All pods logs"
    echo -n "Enter choice [1-3]: "
    read -r log_choice
    
    case $log_choice in
        1)
            info "Viewing application logs..."
            kubectl logs -n airline-app -l app=airline-management --tail=100 -f
            ;;
        2)
            info "Viewing MySQL logs..."
            kubectl logs -n airline-app -l app=mysql --tail=100 -f
            ;;
        3)
            info "Viewing all logs..."
            kubectl logs -n airline-app --all-containers=true --tail=50
            ;;
        *)
            error "Invalid choice"
            ;;
    esac
}

# Scale application
scale_app() {
    echo -n "Enter number of replicas: "
    read -r replicas
    
    if ! [[ "$replicas" =~ ^[0-9]+$ ]]; then
        error "Please enter a valid number"
        return
    fi
    
    info "Scaling application to $replicas replicas..."
    kubectl scale deployment airline-app --replicas="$replicas" -n airline-app
    success "Application scaled successfully!"
    
    kubectl get pods -n airline-app -l app=airline-management
}

# Delete all resources
delete_all() {
    warning "This will delete all resources in the airline-app namespace!"
    echo -n "Are you sure? (yes/no): "
    read -r confirm
    
    if [ "$confirm" == "yes" ]; then
        info "Deleting all resources..."
        kubectl delete namespace airline-app
        success "All resources deleted!"
    else
        info "Deletion cancelled"
    fi
}

# Main script
main() {
    clear
    echo "=========================================="
    echo "  Airline App - GKE Deployment Tool"
    echo "=========================================="
    
    check_kubectl
    check_cluster_connection
    
    while true; do
        show_menu
        read -r choice
        
        case $choice in
            1)
                deploy_all
                ;;
            2)
                deploy_kustomize
                ;;
            3)
                check_status
                ;;
            4)
                get_app_url
                ;;
            5)
                view_logs
                ;;
            6)
                scale_app
                ;;
            7)
                delete_all
                ;;
            8)
                info "Exiting..."
                exit 0
                ;;
            *)
                error "Invalid choice. Please enter 1-8."
                ;;
        esac
        
        echo ""
        echo -n "Press Enter to continue..."
        read -r
        clear
    done
}

# Run main function
main
