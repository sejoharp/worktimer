# about
tool to track working times.

## prerequisites
* clojure: `brew install clojure/tools/clojure`
* babashka: `brew install borkdude/brew/babashka`
* graalvm: `brew install --cask graalvm/tap/graalvm-ce-java11`

## Usage
worktimer help

### Tasks
```
$ bb tasks
The following tasks are available:

release      Replace test destination and copies script to bin directory
install      Install the binary to $HOME/bin directory
run-main     Run main
uberjar      Builds uberjar
run-uber     Run uberjar
graalvm      Checks GRAALVM_HOME env var
native-image Builds native image
```