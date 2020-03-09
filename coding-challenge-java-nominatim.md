# Peakwork Development Challenge - Java / Google Cloud Technologies


## Intro

We would like to challenge you with a small development task in order to get an insight into the way you approach new development tasks and how you apply your skillset.

In particular we are interested in the following aspects:

* Familiarity with Google Cloud Platform services
* Coding style and data structures
* What does production-ready mean to you?

Of course we know that time is a limited resource and therefore we do not expect the given task to be fully done in every way imaginable.
Instead we would suggest that you work on it for a approximately up to 4 hours at maximum. 
Please describe what you consider to be still missing to call the application production-ready afterwards.


## The challenge

As a challenge we would like you to use the OpenStreetmap Nomination API to work with geographic data.
The implemented solution should ...

* ... provide an HTTP endpoint to resolve a geo coordinate to information about that place
* ... use Nominatim as a source for the requested information
* ... store a list of all previously retrieved places
* ... provide an HTTP endpoint which can be queried to inspect these places
* ... store available information about a place including  `coordinate`, `country`, `display_name`, `type`, `oms_id`, `osm_type` if available
* ... should use JSON for payloads
* ... update the information of retrieved places at regular intervals
* ... include testing (of your choice)
* ... be designed to be part of a production environment


### Nominatim

[Nominatim](http://nominatim.org/) is a service to search Open Street Map data which is also hosted as a public service.
The API of this service can be accessed freely (within limits) and without sign-up. You can find the documentation at [http://nominatim.org/release-docs/latest/api/Overview/](http://nominatim.org/release-docs/latest/api/Overview/)


### Conditions

Please use one or more of following techniques and technologies to implement the solution:

#### For microservices

* Java (Version >= 8) as programming language
* Using a micro service framework, e.g. Spring Boot
* Google Kubernetes Engine 

#### For serverless functions

* Google Knative with Java
* Google Cloud Functions with Node.js (8) or Python (3.7)

#### Furthermore, please use at least one additional Google service

* messaging via PubSub, 
* persistence via Datastore, Cloud SQL, ..
* etc.

### Expected deliverables

The project has to contain all source code and additionals like a readme file, which should explaining how to deploy to and run in the cloud as well as how to test locally.

Additionally, the readme should mention what you think is missing to make the project production-ready and what your next steps would be.

## Outro

We are looking forward to the results and hope you have fun with the challenge.
Please provide the whole project, including sources and deliverables, e.g. via GitHub project or as a ZIP archive.