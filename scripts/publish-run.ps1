<#
.SYNOPSIS
  Runs Modrinth + CurseForge publish Gradle tasks for a mod (full or single-loader).

.PARAMETER Mod
  Mod id folder name under mods/ (e.g. xp_stream, saturation_regen).

.PARAMETER Loader
  all = both Fabric and NeoForge (four tasks). fabric | neoforge = that loader only.
  (Use "all" from just; empty string is treated like "all".)
#>
param(
    [Parameter(Mandatory = $true)]
    [string]$Mod,

    [Parameter(Mandatory = $false)]
    [string]$Loader = "all"
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($Loader)) {
    $Loader = "all"
}

$valid = @("all", "fabric", "neoforge")
if ($valid -notcontains $Loader) {
    throw "Invalid loader '$Loader'. Expected 'all', 'fabric', or 'neoforge'."
}

$root = Split-Path -Parent $PSScriptRoot
$gradlew = Join-Path $root "gradlew.bat"
if (-not (Test-Path -LiteralPath $gradlew)) {
    throw "gradlew.bat not found at '$gradlew'."
}

$modProps = Join-Path $root "mods\$Mod\gradle.properties"
if (-not (Test-Path -LiteralPath $modProps)) {
    throw "Unknown mod or missing gradle.properties: '$modProps'."
}

# NeoForge CurseForge: xp_stream registers the publication on :neoforge; saturation_regen uses :publishNeoForge only.
function Get-NeoForgeCurseForgeTask {
    param([string]$ModId)
    switch ($ModId) {
        "xp_stream" { return ":mods:${ModId}:neoforge:publishNeoForgePublicationToCurseForge" }
        "saturation_regen" { return ":mods:${ModId}:publishNeoForge:publishNeoforgePublicationToCurseForge" }
        default {
            throw "publish-run.ps1: add NeoForge CurseForge Gradle task path for mod '$ModId' (see :publishNeoForge:tasks and :neoforge:tasks)."
        }
    }
}

$tasks = [System.Collections.Generic.List[string]]::new()
$neoCf = Get-NeoForgeCurseForgeTask -ModId $Mod

if ($Loader -eq "all") {
    $tasks.Add(":mods:${Mod}:publishNeoForge:modrinth")
    $tasks.Add($neoCf)
    $tasks.Add(":mods:${Mod}:publishFabric:modrinth")
    $tasks.Add(":mods:${Mod}:publishFabric:publishFabricPublicationToCurseForge")
}
elseif ($Loader -eq "fabric") {
    $tasks.Add(":mods:${Mod}:publishFabric:modrinth")
    $tasks.Add(":mods:${Mod}:publishFabric:publishFabricPublicationToCurseForge")
}
else {
    $tasks.Add(":mods:${Mod}:publishNeoForge:modrinth")
    $tasks.Add($neoCf)
}

Write-Host "Running publish tasks for mod '$Mod' (loader: $Loader):"
foreach ($t in $tasks) {
    Write-Host "  $t"
}

& $gradlew @tasks
exit $LASTEXITCODE
