#!/usr/bin/env bash
MY_DIR=$PWD
(SOURCE_DIR=$(basename "$MY_DIR") &&
ZIP=$SOURCE_DIR.zip && # Set variables
pushd .. && # Set parent directory as working directory
rm -f "$SOURCE_DIR/$ZIP" && # remove existing file if exists
zip -r "$ZIP" "$SOURCE_DIR" --exclude "$SOURCE_DIR/$ZIP" --quiet && # ZIP cookiecutter
mv "$ZIP" "$SOURCE_DIR/$ZIP" && # Move ZIP to original directory
popd && # Restore original work directory
echo  "Cookiecutter full path: $PWD/$ZIP") &&
cd "$MY_DIR" || exit 2
