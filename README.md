sears
=====

https://github.com/xxraspberry/sears

A very simple scraper for sears.com search result pages.
See code itself for documentation.

Dependency: jsoup (http://jsoup.org/)

To compile:

    mvn package

To run:

    java -jar target/sears-1.0-jar-with-dependencies.jar "digital books"
    java -jar target/sears-1.0-jar-with-dependencies.jar "digital books" 3

I used Chrome's Inspect Element to empirically figure out the page structure.

Caveat: sears.com may change it's page structure any time. I'm not
watching it closely to keep this repo up to date.
