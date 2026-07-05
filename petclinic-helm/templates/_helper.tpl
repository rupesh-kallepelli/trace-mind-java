{{/*
Expand the chart name.
*/}}
{{- define "petclinic.name" -}}
{{- default .Chart.Name .Values.nameOverride }}
{{- end }}

{{/*
Create a fully qualified app name.
*/}}
{{- define "petclinic.fullname" -}}
{{- if .Values.fullnameOverride }}
{{ .Values.fullnameOverride }}
{{- else }}
{{ printf "%s-%s" .Release.Name (include "petclinic.name" .) }}
{{- end }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "petclinic.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
app.kubernetes.io/name: {{ include "petclinic.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}