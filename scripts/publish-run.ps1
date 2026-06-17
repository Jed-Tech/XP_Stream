<#
.SYNOPSIS
  Runs Modrinth and/or CurseForge publish Gradle tasks for a mod.

.PARAMETER Mod
  Mod id folder name under mods/ (e.g. xp_stream, saturation_regen).

.PARAMETER Loader
  all = both Fabric and NeoForge (four tasks). fabric | neoforge = that loader only.
  (Use "all" from just; empty string is treated like "all".)

.PARAMETER Platform
  all = Modrinth + CurseForge. modrinth | curseforge = only that platform.
#>
param(
    [Parameter(Mandatory = $true)]
    [string]$Mod,

    [Parameter(Mandatory = $false)]
    [string]$Loader = "all",

    [Parameter(Mandatory = $false)]
    [string]$Platform = "all"
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($Loader)) {
    $Loader = "all"
}

$validLoaders = @("all", "fabric", "neoforge")
if ($validLoaders -notcontains $Loader) {
    throw "Invalid loader '$Loader'. Expected 'all', 'fabric', or 'neoforge'."
}

if ([string]::IsNullOrWhiteSpace($Platform)) {
    $Platform = "all"
}

$validPlatforms = @("all", "modrinth", "curseforge")
if ($validPlatforms -notcontains $Platform) {
    throw "Invalid platform '$Platform'. Expected 'all', 'modrinth', or 'curseforge'."
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

function Get-PublishTasks {
    param(
        [string]$ModId,
        [string]$LoaderName,
        [string]$PlatformName
    )

    $tasks = [System.Collections.Generic.List[object]]::new()
    $neoCf = Get-NeoForgeCurseForgeTask -ModId $ModId

    $taskMap = @{
        "fabric" = @{
            "modrinth" = ":mods:${ModId}:publishFabric:modrinth"
            "curseforge" = ":mods:${ModId}:publishFabric:publishFabricPublicationToCurseForge"
        }
        "neoforge" = @{
            "modrinth" = ":mods:${ModId}:publishNeoForge:modrinth"
            "curseforge" = $neoCf
        }
    }

    $loadersToRun = if ($LoaderName -eq "all") { @("neoforge", "fabric") } else { @($LoaderName) }
    $platformsToRun = if ($PlatformName -eq "all") { @("modrinth", "curseforge") } else { @($PlatformName) }

    foreach ($loaderKey in $loadersToRun) {
        foreach ($platformKey in $platformsToRun) {
            $tasks.Add([pscustomobject]@{
                Loader = $loaderKey
                Platform = $platformKey
                Path = $taskMap[$loaderKey][$platformKey]
            })
        }
    }

    return $tasks
}

function Get-RetryCommand {
    param([string]$TaskPath)
    return ".\gradlew.bat ${TaskPath}"
}

$tasks = Get-PublishTasks -ModId $Mod -LoaderName $Loader -PlatformName $Platform
$results = [System.Collections.Generic.List[object]]::new()
$failedTask = $null

Write-Host "Running publish tasks for mod '$Mod' (loader: $Loader, platform: $Platform):"
foreach ($task in $tasks) {
    Write-Host "  [$($task.Loader)/$($task.Platform)] $($task.Path)"
}

foreach ($task in $tasks) {
    Write-Host ""
    Write-Host "Running $($task.Platform) publish for loader '$($task.Loader)':"
    Write-Host "  $($task.Path)"

    & $gradlew $task.Path
    $exitCode = $LASTEXITCODE

    $results.Add([pscustomobject]@{
        Loader = $task.Loader
        Platform = $task.Platform
        Path = $task.Path
        ExitCode = $exitCode
        Status = if ($exitCode -eq 0) { "succeeded" } else { "failed" }
    })

    if ($exitCode -ne 0) {
        $failedTask = $task
        break
    }
}

Write-Host ""
Write-Host "Publish summary for '$Mod':"
foreach ($result in $results) {
    Write-Host "  [$($result.Loader)/$($result.Platform)] $($result.Status): $($result.Path)"
}

if ($failedTask -ne $null) {
    $retryCommand = Get-RetryCommand -TaskPath $failedTask.Path
    $remainingTasks = $tasks | Where-Object {
        $results.Path -notcontains $_.Path
    }

    Write-Host ""
    Write-Host "Publish stopped after a failure."
    Write-Host "  Failed task: $($failedTask.Path)"

    if ($results.Count -gt 1 -or ($results.Count -eq 1 -and $results[0].Status -eq "succeeded")) {
        Write-Host "  Some publish tasks already succeeded. Avoid rerunning the full publish command blindly."
    }

    Write-Host "  Recommended retry:"
    Write-Host "    $retryCommand"

    if ($failedTask.Platform -eq "curseforge") {
        Write-Host "  For more CurseForge logging, rerun with:"
        Write-Host "    $retryCommand --info"
    }

    if ($remainingTasks.Count -gt 0) {
        Write-Host "  Remaining tasks were not attempted:"
        foreach ($remainingTask in $remainingTasks) {
            Write-Host "    [$($remainingTask.Loader)/$($remainingTask.Platform)] $($remainingTask.Path)"
        }
    }

    exit ($results[-1].ExitCode)
}

exit 0
