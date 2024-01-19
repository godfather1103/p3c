#!/bin/sh
idea_versions=("2022.1" "2022.2" "2022.3" "2023.1" "2023.2")
for version in "${idea_versions[@]}"
do
  echo "开始构建$version"
  ./gradlew -Didea_version=$version clean :p3c-idea:buildPlugin
done

echo "构建最新版本"
./gradlew -Didea_version=$version clean :p3c-idea:buildPlugin
