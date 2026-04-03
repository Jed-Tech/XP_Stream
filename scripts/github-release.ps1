param(
    [string]$Mod = "xp_stream",
    [switch]$DryRun
)

$ErrorActionPreference = "Stop"

function Get-PropertyValue {
    param(
        [string]$Path,
        [string]$Name
    )

    $match = Get-Content $Path | Where-Object { $_ -match "^$([regex]::Escape($Name))=" } | Select-Object -First 1
    if (-not $match) {
        throw "Missing property '$Name' in $Path"
    }

    return ($match -split "=", 2)[1].Trim()
}

function Get-ChangelogSection {
    param(
        [string]$Path,
        [string]$Version
    )

    $content = Get-Content $Path -Raw
    $escapedVersion = [regex]::Escape($Version)
    $pattern = "(?ms)^## \[$escapedVersion\].*?(?=^## \[|\z)"
    $match = [regex]::Match($content, $pattern)

    if (-not $match.Success) {
        throw "Could not find CHANGELOG section for version $Version in $Path"
    }

    $lines = @($match.Value -split "`r?`n")
    while ($lines.Count -gt 0 -and ([string]::IsNullOrWhiteSpace($lines[-1]) -or $lines[-1] -eq "---")) {
        $lines = $lines[0..($lines.Count - 2)]
    }

    return ($lines -join [Environment]::NewLine).Trim()
}

$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot "..")).Path
$modPropertiesPath = Join-Path $repoRoot "mods\$Mod\gradle.properties"
$changelogPath = Join-Path $repoRoot "CHANGELOG.md"

if (-not (Test-Path $modPropertiesPath)) {
    throw "Mod properties file not found: $modPropertiesPath"
}

if (-not (Test-Path $changelogPath)) {
    throw "CHANGELOG.md not found: $changelogPath"
}

$modVersion = Get-PropertyValue -Path $modPropertiesPath -Name "mod_version"
$modName = Get-PropertyValue -Path $modPropertiesPath -Name "mod_name"
$tag = "v$modVersion"
$title = "$modName $modVersion"
$notes = Get-ChangelogSection -Path $changelogPath -Version $modVersion

$notesFile = Join-Path ([System.IO.Path]::GetTempPath()) "$Mod-$modVersion-github-release-notes.md"
Set-Content -Path $notesFile -Value $notes -NoNewline

try {
    & gh auth status | Out-Null
    if ($LASTEXITCODE -ne 0) {
        throw "GitHub CLI is not authenticated. Run 'gh auth login' first."
    }

    $headCommit = (& git rev-parse HEAD).Trim()
    if ($LASTEXITCODE -ne 0 -or -not $headCommit) {
        throw "Failed to resolve HEAD commit."
    }

    $tagExists = $false
    & git rev-parse -q --verify "refs/tags/$tag" | Out-Null
    if ($LASTEXITCODE -eq 0) {
        $tagExists = $true
    }

    if ($tagExists) {
        $localTagCommit = (& git rev-list -n 1 $tag).Trim()
        if ($LASTEXITCODE -ne 0 -or -not $localTagCommit) {
            throw "Failed to resolve local tag '$tag'."
        }

        if ($localTagCommit -ne $headCommit) {
            throw "Local tag '$tag' points to $localTagCommit, not HEAD $headCommit."
        }
    }

    $remoteTagExists = $false
    $remoteTagCommit = ""
    $remoteTagLine = (& git ls-remote --exit-code --tags origin "refs/tags/$tag^{}")
    if ($LASTEXITCODE -eq 0) {
        $remoteTagExists = $true
        $remoteTagCommit = ($remoteTagLine -split "\s+")[0].Trim()
    }

    if ($remoteTagExists -and $remoteTagCommit -ne $headCommit) {
        throw "Remote tag '$tag' points to $remoteTagCommit, not HEAD $headCommit."
    }

    $releaseExists = $false
    & cmd /c "gh release view $tag 1>nul 2>nul"
    if ($LASTEXITCODE -eq 0) {
        $releaseExists = $true
    }

    if ($DryRun) {
        Write-Host "Dry run only. No tag or GitHub release will be created."
        Write-Host "Mod: $Mod"
        Write-Host "Version: $modVersion"
        Write-Host "Tag: $tag"
        Write-Host "Title: $title"
        Write-Host "Tag exists: $tagExists"
        Write-Host "Remote tag exists: $remoteTagExists"
        if ($remoteTagExists) {
            Write-Host "Remote tag commit: $remoteTagCommit"
        }
        Write-Host "Release exists: $releaseExists"
        Write-Host "Notes file: $notesFile"
        exit 0
    }

    if ($releaseExists) {
        throw "GitHub release '$tag' already exists."
    }

    if (-not $tagExists) {
        & git tag -a $tag -m $title
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to create git tag '$tag'."
        }

        $tagExists = $true
    }

    if (-not $remoteTagExists) {
        & git push origin "refs/tags/$tag"
        if ($LASTEXITCODE -ne 0) {
            throw "Failed to push git tag '$tag' to origin."
        }
    }

    & gh release create $tag --verify-tag --title $title --notes-file $notesFile
    if ($LASTEXITCODE -ne 0) {
        throw "Failed to create GitHub release '$tag'."
    }

    Write-Host "Created GitHub release '$tag'."
}
finally {
    if (Test-Path $notesFile) {
        Remove-Item -LiteralPath $notesFile -ErrorAction SilentlyContinue
    }
}
