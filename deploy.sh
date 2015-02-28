#!/bin/sh



# get comment
comment="$1"

sbt fullOptJS

if [ "$comment" == "" ]; then
comment="push form CI"
echo "no comment specified to deploy, using default : $comment"
fi

projectName="scalajs-location-tracker"

ghPagesPath="/Users/chandrasekharkode/Desktop/Kode/Programming/scalaprojects/chandu0101.github.io"

projectPath=${ghPagesPath}/${projectName}

mkdir -p ${projectPath}/js

cp index.html ${projectPath}

cp  js/scalajs-location-tracker-opt.js ${projectPath}/js/

cp  js/scalajs-location-tracker-jsdeps.js ${projectPath}/js/

cd ${ghPagesPath}

git add ${projectName}

git commit -m "$comment"

git push