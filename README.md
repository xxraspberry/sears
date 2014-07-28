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

Caveats: 

1. sears.com may change it's page structure any time. I'm not watching it closely to keep this repo up to date.
2. Some keywords (e.g., "digital camera" and "book") seem to be blocked by the site.
