set shell := ["powershell", "-NoProfile", "-Command"]

mods := "xp_stream saturation_regen"
loaders := "fabric neoforge"

default:
  just --list

help:
  just --list

doctor:
  git --version
  gh --version
  java --version
  just --version
  ./gradlew.bat --version

publish-check mod="xp_stream" loader="":
  powershell -NoProfile -Command "if ('{{loader}}' -eq '') { powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\publish-preflight.ps1 -Mod {{mod}} } else { powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\publish-preflight.ps1 -Mod {{mod}} -Loader {{loader}} }"

# Happy-path aliases for xp_stream
xp-stream-fabric-client:
  just run-client xp_stream fabric

xp-stream-fabric-server:
  just run-server xp_stream fabric

xp-stream-neoforge-client:
  just run-client xp_stream neoforge

xp-stream-neoforge-server:
  just run-server xp_stream neoforge

xp-stream-build:
  just build xp_stream

xp-stream-publish:
  just publish xp_stream

xp-stream-publish-check:
  just publish-check xp_stream

xp-stream-github-release:
  just github-release xp_stream

xp-stream-github-release-dry-run:
  just github-release-dry-run xp_stream

# Happy-path aliases for saturation_regen
saturation-regen-fabric-client:
  just run-client saturation_regen fabric

saturation-regen-fabric-server:
  just run-server saturation_regen fabric

saturation-regen-neoforge-client:
  just run-client saturation_regen neoforge

saturation-regen-neoforge-server:
  just run-server saturation_regen neoforge

saturation-regen-build:
  just build saturation_regen

saturation-regen-publish:
  just publish saturation_regen

saturation-regen-publish-check:
  just publish-check saturation_regen

# Parameterized recipes for advanced use
build mod="xp_stream":
  ./gradlew.bat ":mods:{{mod}}:build"

run-client mod="xp_stream" loader="fabric":
  ./gradlew.bat ":mods:{{mod}}:{{loader}}:runClient"

run-server mod="xp_stream" loader="fabric":
  ./gradlew.bat ":mods:{{mod}}:{{loader}}:runServer"

publish mod="xp_stream" loader="":
  powershell -NoProfile -Command "$task = if ('{{loader}}' -eq '') { ':mods:{{mod}}:publishNeoForge:modrinth', ':mods:{{mod}}:publishNeoForge:publishToCurseForge', ':mods:{{mod}}:publishFabric:modrinth', ':mods:{{mod}}:publishFabric:publishToCurseForge' } else { ':mods:{{mod}}:publish' + ('{{loader}}'.Substring(0,1).ToUpper() + '{{loader}}'.Substring(1)) + ':modrinth', ':mods:{{mod}}:publish' + ('{{loader}}'.Substring(0,1).ToUpper() + '{{loader}}'.Substring(1)) + ':publishToCurseForge' }; ./gradlew.bat $task"

github-release mod="xp_stream":
  powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\github-release.ps1 -Mod {{mod}}

github-release-dry-run mod="xp_stream":
  powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\github-release.ps1 -Mod {{mod}} -DryRun

tasks mod="xp_stream" loader="fabric":
  ./gradlew.bat ":mods:{{mod}}:{{loader}}:tasks" --all

jar mod="xp_stream" loader="fabric":
  ./gradlew.bat ":mods:{{mod}}:{{loader}}:jar"

build-all:
  ./gradlew.bat build

clean:
  ./gradlew.bat clean
