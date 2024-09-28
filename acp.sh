#!/bin/bash
# This script performs Add, Commit and Push (ACP) in a single step
# Make sure the script is executable by running 'chmod +x update.sh'
# Also make sure your line endings don´t affect. Solution macOS: tr -d '\r' < acp.sh > acp_fixed.sh
# USAGE: ./acp.sh "Your commit message here"

# Check if commit message is provided
if [ -z "$1" ]; then
    echo "Usage: $0 <commit_message>"
    exit 1
fi

# Perform git add, commit, and push
git add .
git commit -m "$1"
git push
