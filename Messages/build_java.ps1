# These variables you may want to modify
$PROTOC_VERSION = "3.19.4"
$SOURCES = "address.proto", "alert_commands.proto", "alert.proto", "geo.proto", "identifiers.proto", "incident.proto", "resource_status.proto", "resource.proto", "types.proto"

# These variables should typically remain unchanged
$PROTOC_URL = "https://github.com/protocolbuffers/protobuf/releases/download/v$PROTOC_VERSION/protoc-$PROTOC_VERSION-win64.zip"
$PROTOC_DIR = "$PSScriptRoot\.tools\protoc-$PROTOC_VERSION-win64"
$PROTOC = "$PROTOC_DIR\bin\protoc.exe"
$JAVA_OUTPUT = "build/java/src/main/java"

Add-Type -AssemblyName System.IO.Compression.FileSystem

# Check if compiler exists
if (!(Test-Path $PROTOC)) {
    $zip = "${env:TEMP}/protoc.zip"
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest "$PROTOC_URL" -OutFile "$zip"
    [System.IO.Compression.ZipFile]::ExtractToDirectory("$zip", $PROTOC_DIR)
    Remove-Item "$zip"
}

# Generate Java sources
If (Test-Path $JAVA_OUTPUT) {
    Remove-Item $JAVA_OUTPUT -Recurse
}
New-Item -ItemType Directory -Force -Path $JAVA_OUTPUT
& $PROTOC --proto_path=proto --java_out=$JAVA_OUTPUT $SOURCES

# Build JAR using Maven
Set-Location build/java
& mvn clean install

# Move back to original location
Set-Location ..\..\
