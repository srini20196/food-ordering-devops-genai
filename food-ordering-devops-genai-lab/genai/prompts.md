# GenAI practice prompts for DevOps

Use these prompts with sample / non-sensitive logs. Remove credentials, tokens, customer data, private IPs and other sensitive information first.

## Jenkins build failure
"Act as a DevOps troubleshooting assistant. Explain this Jenkins error in simple steps. Identify the first meaningful error, likely causes, commands to verify, and a safe fix. Do not assume the fix is correct without validation. [paste sanitised log]"

## Kubernetes Pod crash
"Analyse this sanitised `kubectl describe pod` output and container log. Explain why the Pod may be restarting. Give read-only diagnostic commands first, then possible fixes. [paste sanitised output]"

## Terraform plan review
"Review this Terraform plan for potentially destructive changes, security concerns and unexpected costs. Explain each risk. Do not tell me to apply until I have checked the plan. [paste plan with secrets removed]"

## Docker image scan
"Explain these Trivy findings by severity. Distinguish OS packages from application dependencies and suggest a safe remediation path. [paste sanitised report]"

## Incident summary
"Summarise this sanitised incident timeline: impact, likely root cause, evidence, mitigation, follow-up actions and prevention. Clearly separate facts from assumptions. [paste timeline]"
