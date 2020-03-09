# Peakwork Coding Challenge

This solution comprises 4 microservices:

1. `nominatim-resolver` resolves geo coordinates via [Nominatim API](http://nominatim.org/release-docs/latest/api/Overview/)
   and publishes resolved places to PubSub topic `nominatim-resolved`
1. `nominatim-save-service` subscribes to `nominatim-resolved-subscription` and writes resolved places into PostgreSQL
1. `nominatim-query-service` reads resolved places from PostgreSQL and provides a query endpoint for them
1. `nominatim-update-service` updates each resolved place ~12 hours after it's last update

## Build

Java >= 11 is required to build this project:

```console
mvnw verify
```

## Deploy

### Create new GCP project (and set as default)

**Note:** If you want to use an existing GCP project instead, you can search & replace the project ID `sebastian-lauth-270620` with your own project ID.

```console
gcloud projects create sebastian-lauth-270620 \
    --organization=$GCP_ORGANIZATION_ID \
    --set-as-default
```

**Important:** billing must be enabled for this newly created project in order for the following commands to work.
This can be done manually via web UI or by listing available billing accounts (`gcloud alpha billing accounts list`) and then associating the newly created project with a billing account (`gcloud alpha billing projects link sebastian-lauth-270620 --billing-account 0X0X0X-0X0X0X-0X0X0X`). 

### Enable required services
```console
gcloud services enable \
    container.googleapis.com \
    containerregistry.googleapis.com \
    pubsub.googleapis.com \
    sql-component.googleapis.com \
    sqladmin.googleapis.com
```

### Create PubSub topic and subscription
```console
gcloud pubsub topics create nominatim-resolved
```
```console
gcloud pubsub subscriptions create nominatim-resolved-subscription --topic=nominatim-resolved
```

### Create PostgreSQL 11 instance
```console
gcloud sql instances create peakwork-db \
    --database-version=POSTGRES_11 \
    --region=europe-west3 \
    --activation-policy=ALWAYS \
    --cpu=1 \
    --memory=3840MiB
```
```console
gcloud sql instances patch peakwork-db --database-flags max_connections=100
```

### Create database user
```console
gcloud sql users create peakwork --instance peakwork-db --password peakwork
```

### Create database
```console
gcloud sql databases create peakwork --instance peakwork-db
```

### Create Kubernetes cluster
```console
gcloud container clusters create peakwork-cluster \
    --zone=europe-west3-b \
    --machine-type=g1-small
```

### Create service account, keys and secret
```console
gcloud iam service-accounts create peakwork-sa
```
```console
gcloud projects add-iam-policy-binding sebastian-lauth-270620 \
    --member serviceAccount:peakwork-sa@sebastian-lauth-270620.iam.gserviceaccount.com \
    --role roles/editor
gcloud projects add-iam-policy-binding sebastian-lauth-270620 \
    --member serviceAccount:peakwork-sa@sebastian-lauth-270620.iam.gserviceaccount.com \
    --role roles/viewer
```
```console
gcloud iam service-accounts keys create credentials.json --iam-account peakwork-sa@sebastian-lauth-270620.iam.gserviceaccount.com
```
```console
kubectl create secret generic cloudsql-instance-credentials --from-file=credentials.json=credentials.json
```

### Build and tag Docker images

Ensure that the Maven build finished successfully before building Docker images.

```console
docker build nominatim-resolver -t gcr.io/sebastian-lauth-270620/nominatim-resolver:1
docker build nominatim-save-service -t gcr.io/sebastian-lauth-270620/nominatim-save-service:1
docker build nominatim-query-service -t gcr.io/sebastian-lauth-270620/nominatim-query-service:1
docker build nominatim-update-service -t gcr.io/sebastian-lauth-270620/nominatim-update-service:1
```

### Push Docker images to Google Container Registry

Add `gcloud` as Docker credential helper, if not already set up:

```console
gcloud auth configure-docker
```

```console
docker push gcr.io/sebastian-lauth-270620/nominatim-resolver:1
docker push gcr.io/sebastian-lauth-270620/nominatim-save-service:1
docker push gcr.io/sebastian-lauth-270620/nominatim-query-service:1
docker push gcr.io/sebastian-lauth-270620/nominatim-update-service:1
```

### Apply Kubernetes deployments
```console
kubectl apply -f nominatim-resolver/deployment.yaml
kubectl apply -f nominatim-save-service/deployment.yaml
kubectl apply -f nominatim-query-service/deployment.yaml
kubectl apply -f nominatim-update-service/deployment.yaml
```

### Expose Kubernetes deployments
```console
kubectl expose deployment nominatim-resolver --type=LoadBalancer
kubectl expose deployment nominatim-save-service --type=LoadBalancer
kubectl expose deployment nominatim-query-service --type=LoadBalancer
kubectl expose deployment nominatim-update-service --type=LoadBalancer
```

## Use

1. query external IPs: `kubectl get services`
   ```console
   kubectl get services
   NAME                       CLUSTER-IP      EXTERNAL-IP      PORT(S)          AGE
   kubernetes                 10.55.240.1     <none>           443/TCP          10m
   nominatim-query-service    10.55.253.14    34.89.249.157    8080:32297/TCP   1m
   nominatim-resolver         10.55.250.176   35.246.138.52    8080:30135/TCP   1m
   nominatim-save-service     10.55.244.120   35.242.212.161   8080:30681/TCP   1m
   nominatim-update-service   10.55.241.176   35.246.240.91    8080:30680/TCP   1m
   ```
1. resolve geo coordinates: `curl -i http://35.246.138.52:8080/resolve\?lat=51.228\&lon=6.7205` (replace IP address with `EXTERNAL-IP` of `nominatim-resolver`)
   ```console
   HTTP/1.1 200 
   Content-Type: application/json
   Transfer-Encoding: chunked
   Date: Mon, 09 Mar 2020 20:08:14 GMT
   
   {"place_id":234232968,"licence":"Data © OpenStreetMap contributors, ODbL 1.0. https://osm.org/copyright","osm_type":"relation","osm_id":1074491,"lat":"51.228066999999996","lon":"6.720360824817666","place_rank":30,"category":"building","type":"yes","importance":0,"addresstype":"building","name":null,"display_name":"9, Rheinallee, Heerdt, Stadtbezirk 4, Düsseldorf, Nordrhein-Westfalen, 40549, Deutschland","address":{"house_number":"9","road":"Rheinallee","suburb":"Heerdt","city_district":"Stadtbezirk 4","city":"Düsseldorf","county":"Düsseldorf","state":"Nordrhein-Westfalen","postcode":"40549","country":"Deutschland","country_code":"de"},"boundingbox":["51.227859","51.2282799","6.7202302","6.7209029"]}
   ```
1. query resolved places: `curl -i http://34.89.249.157:8080/query` or `curl -i http://34.89.249.157:8080/query\?country_code\=de` (replace IP address with `EXTERNAL-IP` of `nominatim-query-service`)

## Improvements needed for production readiness

- application
  - (better) validate input parameters
  - add additional unit, integration and contract tests
  - include REST API documentation (e.g. OpenAPI v3)
  - create possibility to query resolved places via geo coordinates
- observability
  - improve logging; connect to corporate log aggregator
  - add metrics (technical + business); connect to corporate monitoring
  - add tracing; connect to corporate platform
  - capture KPIs (e.g. # of incoming resolve/query requests, successful vs. failed resolves, …)
- operations 
  - setup DNS and SSL termination
  - provide "infrastructure as code" module (e.g. [Terraform](https://www.terraform.io/) or [Google Deployment Manager](https://cloud.google.com/deployment-manager))
  - Kubernetes
    - define CPU and memory requests
    - use dedicated namespace
    - make better use of layers to reduce build duration 
    - try to reduce image size e.g. by using an [Alpine](https://alpinelinux.org/) base image
