# The Weather in Brooklyn - Logos

The Weather in Brooklyn is a machine-generated podcast. This project is the program
that generates logos for each episode based on the forecast for the day.

## Building and Running.

You need: 
- Java 8 / Java 9
- Leiningen (build only)

To build a fat JAR:
`$ lein uberjar`

To run from code:
`$ lein run [args]`

To run from jar:
`java -jar $FATJAR [args]`

This program requires a framebuffer to function because it uses Processing. To run on a headless server, you MUST first run an XVFB framebuffer and set it via the $DISPLAY environment variable.
