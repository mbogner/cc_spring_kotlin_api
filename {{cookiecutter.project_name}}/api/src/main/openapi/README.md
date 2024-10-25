File in this directory will be merged into src/main/resources/spec-api.yml.
That file is then used for code generation. Compilation is dependent on code generation and 
code generation depends on merging the files before.

So DO NOT manually change the resulting file. It is in git to track changes and on classpath to have 
it available during runtime.