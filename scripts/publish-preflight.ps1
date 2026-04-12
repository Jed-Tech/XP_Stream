param(
    [Parameter(Mandatory = $false)]
    [string]$Mod = "xp_stream",

    [Parameter(Mandatory = $false)]
    [string]$Loader = ""
)

$ErrorActionPreference = "Stop"

$validLoaders = @("", "fabric", "neoforge")
if ($validLoaders -notcontains $Loader) {
    throw "Invalid loader '$Loader'. Expected '', 'fabric', or 'neoforge'."
}

$root = Split-Path -Parent $PSScriptRoot
$modPropsPath = Join-Path $root "mods\$Mod\gradle.properties"

if (-not (Test-Path -LiteralPath $modPropsPath)) {
    throw "Unknown mod '$Mod'. Expected gradle.properties at '$modPropsPath'."
}

$props = @{}
Get-Content -LiteralPath $modPropsPath | ForEach-Object {
    if ($_ -match '^\s*#' -or $_ -match '^\s*$') {
        return
    }

    $parts = $_ -split '=', 2
    if ($parts.Count -eq 2) {
        $props[$parts[0].Trim()] = $parts[1].Trim()
    }
}

$modId = $props["mod_id"]
$modName = $props["mod_name"]
$modVersion = $props["mod_version"]
$modrinthProjectId = $props["modrinth_project_id"]
$curseforgeProjectId = $props["curseforge_project_id"]

$modrinthToken = $env:MODRINTH_TOKEN
$curseforgeToken = $env:CURSEFORGE_TOKEN

$issues = New-Object System.Collections.Generic.List[string]
$notes = New-Object System.Collections.Generic.List[string]

if ([string]::IsNullOrWhiteSpace($modId)) {
    $issues.Add("Missing 'mod_id' in '$modPropsPath'.")
}

if ([string]::IsNullOrWhiteSpace($modName)) {
    $issues.Add("Missing 'mod_name' in '$modPropsPath'.")
}

if ([string]::IsNullOrWhiteSpace($modVersion)) {
    $issues.Add("Missing 'mod_version' in '$modPropsPath'.")
}

if ([string]::IsNullOrWhiteSpace($modrinthProjectId)) {
    $issues.Add("Missing 'modrinth_project_id' in '$modPropsPath'.")
}

if ([string]::IsNullOrWhiteSpace($curseforgeProjectId)) {
    $issues.Add("Missing 'curseforge_project_id' in '$modPropsPath'.")
}

if ([string]::IsNullOrWhiteSpace($modrinthToken)) {
    $issues.Add("Environment variable 'MODRINTH_TOKEN' is not set.")
}

if ([string]::IsNullOrWhiteSpace($curseforgeToken)) {
    $issues.Add("Environment variable 'CURSEFORGE_TOKEN' is not set.")
}

Write-Host "Publish preflight"
Write-Host "  Mod: $Mod"
Write-Host "  Loader: $(if ([string]::IsNullOrWhiteSpace($Loader)) { 'all' } else { $Loader })"
Write-Host "  Mod name: $modName"
Write-Host "  Mod version: $modVersion"

if ($issues.Count -gt 0) {
    Write-Host ""
    Write-Host "Blocking issues:"
    foreach ($issue in $issues) {
        Write-Host "  - $issue"
    }

    if ($notes.Count -gt 0) {
        Write-Host ""
        Write-Host "Notes:"
        foreach ($note in $notes) {
            Write-Host "  - $note"
        }
    }

    exit 1
}

Write-Host ""
Write-Host "Ready checks passed:"
Write-Host "  - mod metadata present"
Write-Host "  - Modrinth project ID present"
Write-Host "  - CurseForge project ID present"
Write-Host "  - MODRINTH_TOKEN present"
Write-Host "  - CURSEFORGE_TOKEN present"

if ($notes.Count -gt 0) {
    Write-Host ""
    Write-Host "Notes:"
    foreach ($note in $notes) {
        Write-Host "  - $note"
    }
}
