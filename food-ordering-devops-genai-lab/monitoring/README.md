# Monitoring practice

Suggested learning path after the application is deployed:

1. Prometheus: collect application / infrastructure metrics.
2. Grafana: build dashboards from Prometheus metrics.
3. CloudWatch: inspect AWS / EKS logs, metrics and alarms.
4. ELK / OpenSearch: centralise and search application logs.

For a first lab, begin with Kubernetes basics:

```bash
kubectl get pods -o wide
kubectl describe pod POD_NAME
kubectl logs POD_NAME
kubectl get events --sort-by=.metadata.creationTimestamp
kubectl top pods
```

`kubectl top` and HPA CPU metrics require metrics-server or a compatible metrics provider.
