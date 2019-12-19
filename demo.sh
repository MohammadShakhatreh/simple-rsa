projects='rsa server client'

# First build the projects
./gradlew build

# Make a directory for the demo and go to it
mkdir -p demo;

# unzip the distributions to the demo
for project in $projects; do
  unzip -o "$project/build/distributions/$project.zip" -d demo ;
done
