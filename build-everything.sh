# My bash skills are bad, someone fix these :I

# remove the built-modules folder if it exists
if [ -d built-modules ]; then
  rm -rf built-modules
fi

# create the built-modules folder
mkdir built-modules

# loop for each directories (which can be a module's source code or the submodules folder)
for d in */; do
  cd "$d"

  # checking if this is a correct module source code and not the submodule / built-modules folder
  if [ -f gradlew ]; then
    curdir=${PWD##*/}

    printf " ======= Module $curdir\n"

    # Compile this module
    printf " ===== Compiling $curdir\n"
    ./gradlew assembleDebug

    # Then package it into a module zip
    printf "\n ===== Pacakging $curdir\n"
    cp module/build/outputs/apk/debug/module-debug.apk module.jar
    zip "../built-modules/$curdir.zip" module.jar manifest.json

    printf "\n ===== Cleaning up $curdir\n"
    # Cleanup
    rm module.jar
    ./gradlew clean

    printf "\n ===== Done\n"
    printf "  $curdir is built\n\n\n"
  fi

  cd ..
done

printf "Building everything finished\n"
