#!/bin/bash

# These variables you may want to modify
PROTOC_VERSION="3.19.4"

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

# These variables should typically remain unchanged
PROTOBUF_URL="https://github.com/protocolbuffers/protobuf/releases/download/v$PROTOC_VERSION/protobuf-cpp-$PROTOC_VERSION.tar.gz"
TOOLS_DIR="$SCRIPT_DIR/.tools"
PROTOBUF_DIR="$TOOLS_DIR/protobuf-$PROTOC_VERSION"
PROTOBUF_OUTPUT_DIR="$PROTOBUF_DIR-bin"
PROTOC="$PROTOBUF_OUTPUT_DIR/bin/protoc"
CPP_DIR="$SCRIPT_DIR/build/cpp"
CPP_OUTPUT="$CPP_DIR/src"

# Check if protobuf compiler exists and compile it if not
if [ ! -f "$PROTOC" ]; then
    echo "Could not find Protocol Buffer compiler $PROTOC"
    # Check if protobuf sources exist and download them if not
    if [ ! -f "$PROTOBUF_DIR" ]; then
        echo "Downloading $PROTOBUF_URL"
        ARCHIVE="/tmp/protobuf.tar.gz"
        curl -L "$PROTOBUF_URL" > "$ARCHIVE"
        mkdir -p "$PROTOBUF_DIR"
        tar xfz "$ARCHIVE" -C "$TOOLS_DIR"
    fi

    echo "Compiling Protocol Buffer compiler"
    cd "$PROTOBUF_DIR"
    ./configure --prefix="$PROTOBUF_OUTPUT_DIR"
    make -j$(nproc)
    # skip make check as it takes too long and requires too much disk space for a small SD card in a Raspberry Pi
    make install
    make clean
fi

cd "$SCRIPT_DIR"

# Generate C++ sources
echo "Generating C++ sources"
if [ -f "$CPP_OUTPUT" ]; then
    rm -fr "$CPP_OUTPUT"
fi
mkdir -p "$CPP_OUTPUT"
SOURCES=(proto/*.proto)
printf -v SOURCES_STRING "%s " "${SOURCES[@]}"
"$PROTOC" -I=proto --cpp_out="$CPP_OUTPUT" $SOURCES_STRING

# Compile C++ sources
echo "Compiling C++ sources"
cd "$CPP_DIR"
make clean all

# Go back to original directory
cd "$SCRIPT_DIR"
